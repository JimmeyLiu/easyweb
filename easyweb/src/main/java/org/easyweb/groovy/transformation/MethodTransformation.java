package org.easyweb.groovy.transformation;

import org.easyweb.app.App;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.SourceUnit;

public abstract class MethodTransformation {

	public MethodTransformation() {
		MethodTransformationInvoker.add(this);
	}

	public abstract void transformat(SourceUnit sourceUnit, MethodNode methodNode,App app);

}
