package com.thoaikx;

import static com.thoaikx.ultis.RecordUtils.startRecordATU;

import com.thoaikx.dataprovider.Fixture;
import com.thoaikx.ultis.RecordUtils;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


@Log4j2
public class Test01 extends BaseWeb {

    @BeforeClass
    public void  start() throws Exception {
        startRecordATU("video-01");
    }

    @AfterClass
    public void  stop() throws Exception {
        RecordUtils.stopRecordATU();
    }
    @Test
    public void test02() {

    }

    @Test(dataProvider = "csv",dataProviderClass = Fixture.class)
    public  void  testDataProvider(String name , String age , String city) {
        System.out.println("Paremeter is" + name + age + city);
    }
}
