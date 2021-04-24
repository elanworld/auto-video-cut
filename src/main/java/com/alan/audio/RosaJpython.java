package com.alan.audio;

import java.util.Hashtable;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import com.alan.common.util.Output;

@Deprecated
public class RosaJpython {
	public static void main(String[] args) throws Exception {
		PythonInterpreter pythonInterpreter = new PythonInterpreter();
		pythonInterpreter.exec("print('Hello Baeldung Readers!!')");
		pythonInterpreter.exec("import librosa");
		pythonInterpreter.exec("var = 55");
		pythonInterpreter.exec("dic = {'55':'66'}");
		PyObject var = pythonInterpreter.get("var");
		PyObject locals = pythonInterpreter.get("dic");
		Hashtable<Integer, Integer> integerIntegerHashtable = new Hashtable<Integer, Integer>();
		String s = var.toString();
		Output.print(s, integerIntegerHashtable);
	}
}
