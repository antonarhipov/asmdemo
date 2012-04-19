package zt.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;

public class MyClassLoader extends ClassLoader {

    @Override
    protected Class findClass(String name) throws ClassNotFoundException {
        try {
            ClassReader cr = new ClassReader(name);
            ClassWriter cw = new ClassWriter(cr,
                    ClassWriter.COMPUTE_MAXS |
                            ClassWriter.COMPUTE_FRAMES);

            MyClassAdaptor ca = new MyClassAdaptor(cw);

            cr.accept(ca, ClassReader.EXPAND_FRAMES);

            byte b[] = cw.toByteArray();
            return defineClass(name, b, 0, b.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadClass(name);
    }

}
