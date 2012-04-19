package zt.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

public class MyMethodAdapter extends AdviceAdapter {

    private String name;
    private boolean trace;

    protected MyMethodAdapter(MethodVisitor mv, int access, String name, String desc) {
        super(mv, access, name, desc);
        this.name = name;
        this.mv = mv;
    }


    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if(desc.equalsIgnoreCase("Lzt/asm/Trace;") && visible){
            this.trace = true;
        }

        return super.visitAnnotation(desc, visible);
    }

    @Override
    protected void onMethodEnter() {
        if(trace)
            trace("enter");
    }

    @Override
    protected void onMethodExit(int opcode) {
        if(trace)
            trace("return");
    }

    private void trace(String action) {
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("------" + action + " " + name + "-----");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
        mv.visitEnd();
    }


    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);
        if (opcode == ISTORE) {
            visitTrace(var);
        }
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        super.visitIincInsn(var, increment);
        visitTrace(var);
    }

    private void visitTrace(int var) {
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitVarInsn(ILOAD, var);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V");
        mv.visitEnd();
    }
}
