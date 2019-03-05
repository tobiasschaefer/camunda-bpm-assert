package org.camunda.bpm.engine.test.assertions.bpmn;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.assertions.helpers.Failure;
import org.camunda.bpm.engine.test.assertions.helpers.ProcessAssertTestCase;
import org.junit.Rule;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import static org.junit.Assert.fail;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public class ProcessEngineTestsCalledProcessInstanceTest extends ProcessAssertTestCase {

  @Rule
  public ProcessEngineRule processEngineRule = new ProcessEngineRule();

  @Test
  @Deployment(resources = {"bpmn/ProcessEngineTests-calledProcessInstance-superProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess2.bpmn"
  })
  public void testCalledProcessInstanceNoArgs_CalledTooEarly_Failure() {
    // Given
    final String processDefinitionKey = "ProcessEngineTests-calledProcessInstance-superProcess1";
    runtimeService().startProcessInstanceByKey(processDefinitionKey);
    // Then
    assertFailureOnCalledProcessInstance(new Runnable() {
      @Override
      public void run() {
        calledProcessInstance();
      }
    });
  }
  
  @Test
  @Deployment(resources = {"bpmn/ProcessEngineTests-calledProcessInstance-superProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess2.bpmn"
  })
  public void testCalledProcessInstanceArgProcessInstance_CalledTooEarly_Success() {
    // Given
    final String processDefinitionKey = "ProcessEngineTests-calledProcessInstance-superProcess1";
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(processDefinitionKey);
    // Then
    assertThat(calledProcessInstance(processInstance)).isNotNull();
  }
  
  @Test
  @Deployment(resources = {"bpmn/ProcessEngineTests-calledProcessInstance-superProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess2.bpmn"
  })
  public void testCalledProcessInstanceArgProcessInstanceQuery_CalledTooEarly_Failure() {
    // Given
    final String processDefinitionKey = "ProcessEngineTests-calledProcessInstance-superProcess1";
    runtimeService().startProcessInstanceByKey(processDefinitionKey);
    // Then
    assertFailureOnCalledProcessInstance(new Runnable() {
      @Override
      public void run() {
        calledProcessInstance(processInstanceQuery());
      }
    });
  }
  
  @Test
  @Deployment(resources = {"bpmn/ProcessEngineTests-calledProcessInstance-superProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess2.bpmn"
  })
  public void testCalledProcessInstanceArgProcessDefinitionKey_CalledTooEarly_Failure() {
    // Given
    final String processDefinitionKey = "ProcessEngineTests-calledProcessInstance-superProcess1";
    runtimeService().startProcessInstanceByKey(processDefinitionKey);
    // Then
    assertFailureOnCalledProcessInstance(new Runnable() {
      @Override
      public void run() {
        calledProcessInstance(processDefinitionKey);
      }
    });
  }
  
  @Test
  @Deployment(resources = {"bpmn/ProcessEngineTests-calledProcessInstance-superProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess2.bpmn"
  })
  public void testCalledProcessInstanceArgProcessDefinitionKeyAndProcessInstance_CalledTooEarly_Success() {
    // Given
    final String processDefinitionKey = "ProcessEngineTests-calledProcessInstance-superProcess1";
    final String subProcessDefinitionKey = "ProcessEngineTests-calledProcessInstance-subProcess1";
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(processDefinitionKey);
    // Then
    assertThat(calledProcessInstance(subProcessDefinitionKey, processInstance)).isNotNull();
  }
  
  @Test
  @Deployment(resources = {"bpmn/ProcessEngineTests-calledProcessInstance-superProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess2.bpmn"
  })
  public void testCalledProcessInstanceArgProcessInstanceQueryAndProcessInstance_CalledTooEarly_Success() {
    // Given
    final String processDefinitionKey = "ProcessEngineTests-calledProcessInstance-superProcess1";
    final String subProcessDefinitionKey = "ProcessEngineTests-calledProcessInstance-subProcess1";
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(processDefinitionKey);
    // Then
    assertThat(calledProcessInstance(processInstanceQuery().processDefinitionKey(subProcessDefinitionKey), processInstance))
      .isNotNull();
  }

  private void assertFailureOnCalledProcessInstance(Runnable runnable) {
    try {
      runnable.run();
      fail("call to calledProcessInstance() should have thrown an error");
    } catch (IllegalStateException iae) {
      // expected
    }
  }
  
  @Test
  @Deployment(resources = {"bpmn/ProcessEngineTests-calledProcessInstance-superProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess2.bpmn"
  })
  public void testCalledProcessInstance_FirstOfTwoSequential_Success() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-calledProcessInstance-superProcess1"
    );
    // When
    assertThat(processInstance)
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-superProcess1");
    // Then
    assertThat(calledProcessInstance())
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess1");
    // And
    assertThat(calledProcessInstance(processInstance))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess1");
    // When
    assertThat(processInstance)
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-superProcess1");
    // And
    assertThat(calledProcessInstance("ProcessEngineTests-calledProcessInstance-subProcess1"))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess1");
    // And
    assertThat(calledProcessInstance("ProcessEngineTests-calledProcessInstance-subProcess1", processInstance))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess1");
    // When
    assertThat(processInstance)
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-superProcess1");
    // And
    assertThat(calledProcessInstance(processInstanceQuery().processDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess1")))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess1");
    // And
    assertThat(calledProcessInstance(processInstanceQuery().processDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess1"), processInstance))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess1");
  }

  @Test
  @Deployment(resources = {"bpmn/ProcessEngineTests-calledProcessInstance-superProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess2.bpmn"
  })
  public void testCalledProcessInstance_SecondOfTwoSequential_Success() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-calledProcessInstance-superProcess1"
    );
    // And
    complete(task("UserTask_1", calledProcessInstance(processInstance)));
    // When
    assertThat(processInstance)
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-superProcess1");
    // Then
    assertThat(calledProcessInstance())
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess2");
    // And
    assertThat(calledProcessInstance(processInstance))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess2");
    // When
    assertThat(processInstance)
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-superProcess1");
    // And
    assertThat(calledProcessInstance("ProcessEngineTests-calledProcessInstance-subProcess2"))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess2");
    // And
    assertThat(calledProcessInstance("ProcessEngineTests-calledProcessInstance-subProcess2", processInstance))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess2");
    // When
    assertThat(processInstance)
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-superProcess1");
    // And
    assertThat(calledProcessInstance(processInstanceQuery().processDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess2")))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess2");
    // And
    assertThat(calledProcessInstance(processInstanceQuery().processDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess2"), processInstance))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess2");
  }

  @Test
  @Deployment(resources = {"bpmn/ProcessEngineTests-calledProcessInstance-superProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess2.bpmn"
  })
  public void testCalledProcessInstance_SecondOfTwoSequential_Failure() {
    // Given
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-calledProcessInstance-superProcess1"
    );
    // And
    complete(task("UserTask_1", calledProcessInstance(processInstance)));
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(calledProcessInstance())
          .isNotNull();
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(calledProcessInstance("ProcessEngineTests-calledProcessInstance-subProcess2"))
          .isNotNull();
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(calledProcessInstance(processInstanceQuery().processDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess2")))
          .isNotNull();
      }
    });
    // When
    assertThat(processInstance)
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-superProcess1");
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(calledProcessInstance())
          .isNull();
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(calledProcessInstance("ProcessEngineTests-calledProcessInstance-subProcess1"))
          .isNotNull();
      }
    });
    // And
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(calledProcessInstance(processInstanceQuery().processDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess1")))
          .isNotNull();
      }
    });
  }

  @Test
  @Deployment(resources = {"bpmn/ProcessEngineTests-calledProcessInstance-superProcess2.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess2.bpmn"
  })
  public void testCalledProcessInstance_TwoOfTwoParallel_Success() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-calledProcessInstance-superProcess2"
    );
    // Then
    assertThat(calledProcessInstance("ProcessEngineTests-calledProcessInstance-subProcess1", processInstance))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess1");
    // And
    assertThat(calledProcessInstance(processInstanceQuery().processDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess1"), processInstance))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess1");
    // And
    assertThat(calledProcessInstance("ProcessEngineTests-calledProcessInstance-subProcess2", processInstance))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess2");
    // And
    assertThat(calledProcessInstance(processInstanceQuery().processDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess2"), processInstance))
      .hasProcessDefinitionKey("ProcessEngineTests-calledProcessInstance-subProcess2");
  }

  @Test
  @Deployment(resources = {"bpmn/ProcessEngineTests-calledProcessInstance-superProcess2.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess1.bpmn", "bpmn/ProcessEngineTests-calledProcessInstance-subProcess2.bpmn"
  })
  public void testCalledProcessInstance_TwoOfTwoParallel_Failure() {
    // When
    final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(
      "ProcessEngineTests-calledProcessInstance-superProcess2"
    );
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(calledProcessInstance("ProcessEngineTests-calledProcessInstance-subProcess3", processInstance))
          .isNotNull();
      }
    });
  }

}
