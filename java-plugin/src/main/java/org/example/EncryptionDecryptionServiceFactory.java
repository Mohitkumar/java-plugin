package org.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class EncryptionDecryptionServiceFactory {

    public static EncryptionDecryptionService getEncryptionDecryptionService(String className, String key, String iv){
        try {
            Class<?> aClass = Class.forName(className);

            Object targetObj = aClass.getConstructor(String.class, String.class).newInstance(key, iv);

            EncryptionDecryptionService encService = (EncryptionDecryptionService) java.lang.reflect.Proxy.newProxyInstance(
                    EncryptionDecryptionService.class.getClassLoader(),
                    new Class[] {EncryptionDecryptionService.class },
                    new EncryptionDecryptionHandler(aClass, targetObj, key, iv));

            return encService;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static class EncryptionDecryptionHandler implements InvocationHandler{

        private Class<?> encDecClass;

        private Object targetObject;

        private String key;

        private String iv;

        public EncryptionDecryptionHandler(Class<?> encDecClass, Object targetObject, String key, String iv) {
            this.encDecClass = encDecClass;
            this.targetObject = targetObject;
            this.key = key;
            this.iv = iv;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();

            Method targetClassMethod = encDecClass.getMethod(name, String.class);
            return  targetClassMethod.invoke(targetObject, args);
        }
    }

}
