package com.github.navid1981.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.MemberValue;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class RequiredAnnotationService {

    @Value("${java.model.path}")
    private String path;

    @Value("${java.package}")
    private String packageName;

    @Autowired
    private URLClassLoader urlClassLoader;

    public void addRequiredAnnotation(String className, String fieldName) throws ClassNotFoundException {
        addAnnotationToField(className,fieldName, JsonProperty.class);
    }

    private void addAnnotationToField(String className, String fieldName, Class<?> annotationClass) throws ClassNotFoundException {
        Class<?> clazz = urlClassLoader.loadClass(packageName+"."+className);
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass;
        try {
//            ctClass = pool.getCtClass(clazz.getName());
            ctClass = pool.makeClass(new FileInputStream(path+"/"+packageName.replace(".","/")+"/"+className+".class"));
            if (ctClass.isFrozen()) {
                ctClass.defrost();
            }
            CtField ctField = ctClass.getDeclaredField(fieldName);
            ConstPool constPool = ctClass.getClassFile().getConstPool();

//            Annotation annotation = new Annotation(annotationClass.getName(), constPool);

            AnnotationsAttribute attr = getAnnotationsAttributeFromField(ctField);
            if (attr == null) {
                attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
                ctField.getFieldInfo().addAttribute(attr);
                return;
            }
            Annotation annotation = attr.getAnnotation(JsonProperty.class.getName());
            MemberValue booleanValue=new BooleanMemberValue(true,constPool);
            annotation.addMemberValue("required",booleanValue);


            attr.addAnnotation(annotation);

            retransformClass(clazz, ctClass.toBytecode());
            if (ctClass.isFrozen()) {
                ctClass.defrost();
            }
        } catch (NotFoundException | IOException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

    private AnnotationsAttribute getAnnotationsAttributeFromField(CtField ctField) {
        List<AttributeInfo> attrs = ctField.getFieldInfo().getAttributes();
        AnnotationsAttribute attr = null;
        if (attrs != null) {
            Optional<AttributeInfo> optional = attrs.stream()
                    .filter(AnnotationsAttribute.class::isInstance)
                    .findFirst();
            if (optional.isPresent()) {
                attr = (AnnotationsAttribute) optional.get();
            }
        }
        return attr;
    }

    private void retransformClass(Class<?> clazz, byte[] byteCode) {
        ClassFileTransformer cft = new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                return byteCode;
            }
        };

        Instrumentation instrumentation = ByteBuddyAgent.install();
        try {
            instrumentation.addTransformer(cft, true);
            instrumentation.retransformClasses(clazz);
        } catch (UnmodifiableClassException e) {
            e.printStackTrace();
        } finally {
            instrumentation.removeTransformer(cft);
        }
    }
}
