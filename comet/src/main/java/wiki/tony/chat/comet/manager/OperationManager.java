package wiki.tony.chat.comet.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import wiki.tony.chat.comet.operation.Operation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tony on 4/14/16.
 */
@Component
public class OperationManager {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<Integer, Operation> ops = new ConcurrentHashMap<Integer, Operation>();

    @Bean(name = "operations")
    public Map<Integer, Operation> operations() {
        Map<String, Operation> beans = applicationContext.getBeansOfType(Operation.class);
        for (Operation op : beans.values()) {
            ops.put(op.op(), op);
        }
        return ops;
    }

    public Operation find(Integer op) {
        return ops.get(op);
    }

}
