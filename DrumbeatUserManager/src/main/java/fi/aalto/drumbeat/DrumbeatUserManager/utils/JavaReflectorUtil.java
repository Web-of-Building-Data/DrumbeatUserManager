package fi.aalto.drumbeat.DrumbeatUserManager.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.viceversatech.rdfbeans.annotations.RDFSubject;

import fi.aalto.drumbeat.DrumbeatUserManager.UserManagerUI;
import fi.aalto.drumbeat.DrumbeatUserManager.entities.Employee;
import fi.aalto.drumbeat.DrumbeatUserManager.utils.tags.DrumbeatFormField;

/*
* 
Jyrki Oraskari, Aalto University, 2016 

This research has partly been carried out at Aalto University in DRUMBEAT 
“Web-Enabled Construction Lifecycle” (2014-2017) —funded by Tekes, 
Aalto University, and the participating companies.

The MIT License (MIT)
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/


public class JavaReflectorUtil {

	public void test() {
		fi.aalto.drumbeat.DrumbeatUserManager.entities.Employee e = new Employee(null);
		fields(e);
		List<ValuePair> lista = fields(e);
		System.out.println("Lista len:" + lista.size());
		for (ValuePair vp : lista) {
			System.out.println(vp.getName() + ":" + vp.getValue());
			vp.setValue("test");
		}
	}

	static public List<ValuePair> annotated_fields(Object instance) {
		List<ValuePair> ret = new ArrayList<ValuePair>();
		Method method[] = instance.getClass().getMethods();
		Map<String, Method> method_map = new HashMap<String, Method>();

		for (int j = 0; j < method.length; j++)
			method_map.put(method[j].getName(), method[j]);
		for (int j = 0; j < method.length; j++) {
			try {

				Annotation[] annotations = method[j].getDeclaredAnnotations();

				boolean has_df_annotation = false;
				for (Annotation annotation : annotations) {
					if (annotation instanceof DrumbeatFormField) {
						has_df_annotation = true;
					}
				}

				if (has_df_annotation) {
					Object o = method[j].invoke(instance);
					if (String.class.isInstance(o)) {
						String setname = 's' + method[j].getName().substring(1);
						Method setm = method_map.get(setname);
						if (setm != null)
							ret.add(new ValuePair(method[j].getName().substring(3), o, setm, instance));
					} else if (o == null) {
						String setname = 's' + method[j].getName().substring(1);
						Method setm = method_map.get(setname);
						if (setm != null)
							ret.add(new ValuePair(method[j].getName().substring(3), "", setm, instance));

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static void setHostName(Object o)
	{
		String host_url=UserManagerUI.base_dataURI;
		
		change_RDFSubject_annotation(o, "getId", RDFSubject.class,"prefix", host_url);
	}
	
	
	// http://stackoverflow.com/questions/14268981/modify-a-class-definitions-annotation-string-parameter-at-runtime
	@SuppressWarnings("unchecked")
	public static Object changeAnnotationValue(Annotation annotation, String key, String newValue) {
		if(annotation==null)
			return null;
		Object handler = Proxy.getInvocationHandler(annotation);
		Field f;
		try {
			f = handler.getClass().getDeclaredField("memberValues");
		} catch (NoSuchFieldException | SecurityException e) {
			throw new IllegalStateException(e);
		}
		f.setAccessible(true);
		Map<String, Object> memberValues;
		try {
			memberValues = (Map<String, Object>) f.get(handler);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
		Object oldValue = memberValues.get(key);
		if (oldValue == null || oldValue.getClass() != newValue.getClass()) {
			throw new IllegalArgumentException();
		}
		memberValues.put(key, newValue);
		return oldValue;
	}

	static public void change_RDFSubject_annotation(Object instance, String method_name, Class annotation_class,
			String key, String new_value) {
		Method method;
		try {
			method = instance.getClass().getMethod(method_name);
			final Annotation methodAnnotation = method.getAnnotation(annotation_class);
			changeAnnotationValue(methodAnnotation, key, new_value);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	static public List<ValuePair> fields(Object instance) {
		List<ValuePair> ret = new ArrayList<ValuePair>();
		Method method[] = instance.getClass().getMethods();
		Map<String, Method> method_map = new HashMap<String, Method>();
		for (int j = 0; j < method.length; j++)
			method_map.put(method[j].getName(), method[j]);
		for (int j = 0; j < method.length; j++) {
			try {
				if (method[j].getName().startsWith("get")) {
					Object o = method[j].invoke(instance);
					if (String.class.isInstance(o)) {
						String setname = 's' + method[j].getName().substring(1);
						Method setm = method_map.get(setname);
						if (setm != null)
							ret.add(new ValuePair(method[j].getName().substring(3), o, setm, instance));
					} else if (o == null) {
						String setname = 's' + method[j].getName().substring(1);
						Method setm = method_map.get(setname);
						if (setm != null)
							ret.add(new ValuePair(method[j].getName().substring(3), "", setm, instance));

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		new JavaReflectorUtil();
	}
}
