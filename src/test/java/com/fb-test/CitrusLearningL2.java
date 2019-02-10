/*
 * Copyright 2006-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fbtest;


import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.http.server.HttpServer;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.TestCaseMetaInfo;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.testng.annotations.Test;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import com.consol.citrus.dsl.runner.AbstractTestBehavior;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.actions.AbstractTestAction;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;




/**
 * @author FBo
 */

@Test
public class CitrusLearningL2 extends TestNGCitrusTestRunner {



    public class myBehavior extends AbstractTestBehavior {

        private String payloadData;

        myBehavior withPayloadData(String payload) {
            this.payloadData = payload;
            return this;
        }

        @Override
        public void apply() {
            echo("[behavior] - OK ->behavior is invoked");
            echo("[behavior]" + payloadData + " - OK ->variable from Test is correctly transmitted to behavior");
            echo(func_asis(payloadData));
            echo(func_replace(payloadData));  // if you uncomment this line the test will crash at starting time when invoking replace_all
        }


        String func_asis(String myvar)
        {
          String s = "This is a string in which nothing is replaced, OK fine !";
          echo("[func_asis] OK ->in func_asis now ");
          echo("[func_asis] myvar="+ myvar + " - OK ->variable from Test is correctly transmitted to func_asis");
 
 
          return s;
        }

        String func_replace(String myvar)
        {
          String s = "This is a string in which to replace !!Name!! by the value of my citrus variable but it crashes";
          echo("[func_replace] OK ->in func_replace");
          echo("[func_replace] myvar="+ myvar + " - OK ->variable from Test is correctly transmitted to func_asis");
          //s=s.replaceAll("!!Name!!",myvar); // This will crash when starting the test (not actually when running it) !!!
          return s;
        }

    }

    @CitrusTest 
    public void mySimpleTest() throws IOException {
      description("Simple Test invoking a behavior which it self will invoke a java function");
      variable("vm", "/dc/vm/folder/vm_basename");
 
      repeat().until("i = 3")
            .actions(
              sleep(1000L),
              applyBehavior(new myBehavior().withPayloadData("${vm}${i}"))
            ); 
    }



}

