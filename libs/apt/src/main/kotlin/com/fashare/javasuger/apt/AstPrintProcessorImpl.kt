package com.fashare.javasuger.apt

import com.fashare.javasuger.annotation.AstPrint
import com.fashare.javasuger.apt.base.BaseProcessor
import com.fashare.javasuger.apt.util.logd
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.tree.TreeTranslator
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

internal class AstPrintProcessorImpl : BaseProcessor() {

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(AstPrint::class.java.canonicalName)
    }

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        logd("process begin !!! set = $set")

        roundEnvironment.getElementsAnnotatedWith(AstPrint::class.java)
                .filter { it is TypeElement }
                .map { it as TypeElement }
                .forEach {
                    it.enclosingElement
                    var ele: Element? = it
                    do {
                        logd("process find class = $ele, jcTree = ${ele?.javaClass?.simpleName}")
                        ele = ele?.enclosingElement
                    } while (ele != null)
//                    val tree = trees.getTree(it) as JCTree
                    val treePath = trees.getPath(it)

                    logd("  treePath.leaf = ${treePath.leaf.javaClass.simpleName}, treePath.cu = ${treePath.compilationUnit.javaClass.simpleName}")
//                    logd("process find class = $it, jcTree = ${tree.javaClass.simpleName}")
//                    tree.accept(MyTreeTranslator())
                }

        logd("process end !!!")
        return true
    }

    inner class MyTreeTranslator : TreeTranslator() {

        override fun visitTopLevel(cu: JCTree.JCCompilationUnit) {
            printJcTree(cu)
            printJcTree("cu.defs", cu.defs)

            super.visitTopLevel(cu)
        }

        override fun visitClassDef(jcClassDecl: JCTree.JCClassDecl) {
            super.visitClassDef(jcClassDecl)

            printJcTree(jcClassDecl)
            printJcTree("jcClassDecl.defs", jcClassDecl.defs)
        }

        private fun printJcTree(msg: String, jcTree: List<JCTree>) {
            logd("  printJcTree: msg = $msg: ")
            jcTree.forEach {
                printJcTree(it)
            }
        }

        private fun printJcTree(jcTree: JCTree?) {
            logd("  printJcTree: cur = ${jcTree?.javaClass?.simpleName ?: "null"}, $jcTree")

        }
    }
}


