package com.alibaba.jvm.sandbox.demo;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ProcessController;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;

@MetaInfServices(Module.class)
@Information(id = "broken-clock-tinker")
public class BrokenClockTinkerModule implements Module {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Command("repairCheckState")
    public void repairCheckState() {

        new EventWatchBuilder(moduleEventWatcher)
                .onClass("com.taobao.demo.Clock")
                .onBehavior("checkState")
                .onWatch(new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Throwable {
                        // 在此，你可以通过ProcessController来改变原有方法的执行流程
                        // 这里的代码意义是：改变原方法抛出异常的行为，变更为立即返回；void返回值用null表示
                        ProcessController.returnImmediately(null);
                    }
                }, Event.Type.THROWS);

    }

}
