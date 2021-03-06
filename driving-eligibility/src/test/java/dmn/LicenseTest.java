package dmn;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;

public class LicenseTest {
	private DMNRuntime dmnRuntime;
	private DMNModel dmnModel;
	private DMNContext dmnContext;

	@Before
	public void before() {
		KieServices kieServices = KieServices.Factory.get();

		KieContainer kieContainer = kieServices.getKieClasspathContainer();
		dmnRuntime = kieContainer.newKieSession().getKieRuntime( DMNRuntime.class );
		dmnModel = dmnRuntime.getModel("http://www.trisotech.com/definitions/_90a17b17-c884-4fa9-ba59-7a47899d89b2", "driving-eligibility-2");
		
		dmnContext = dmnRuntime.newContext();		
	}

	
	@Test
	public void testNotUS() {
		Map<String, Object> person = new HashMap<>();
		
		person.put("name", "Donato");
		person.put("age", 17);
		person.put("country", "italy");
		dmnContext.set("Person", person);

		DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
		Assert.assertEquals(dmnResult.getDecisionResults().size(), 1);
		Assert.assertEquals(dmnResult.getDecisionResults().get(0).getResult(), Boolean.FALSE);

		person.replace("age", 18);
		dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);

		Assert.assertEquals(dmnResult.getDecisionResults().size(), 1);
		Assert.assertEquals(dmnResult.getDecisionResults().get(0).getResult(), Boolean.TRUE);
	}

	@Test
	public void testUS() {
		Map<String, Object> person = new HashMap<>();
		
		person.put("name", "Jim");
		person.put("age", 15);
		person.put("country", "us");
		dmnContext.set("Person", person);

		DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
		Assert.assertEquals(dmnResult.getDecisionResults().size(), 1);
		Assert.assertEquals(dmnResult.getDecisionResults().get(0).getResult(), Boolean.FALSE);

		person.replace("age", 16);
		dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);

		Assert.assertEquals(dmnResult.getDecisionResults().size(), 1);
		Assert.assertEquals(dmnResult.getDecisionResults().get(0).getResult(), Boolean.TRUE);
	}
	
}
