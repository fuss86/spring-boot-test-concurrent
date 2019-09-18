package hello;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController
{

    private static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private ApplicationContext applicationContext;

    private ThreadLocal<ApplicationContext> contextThreadLocal = new ThreadLocal<>();

    @PostConstruct
    public void postConstruct()
    {
        logger.info(String.format("%s.postConstruct [%s][%s]", HelloController.class, this, applicationContext));
        contextThreadLocal.set(applicationContext);
    }

    @PreDestroy
    public void preDestroy()
    {
        contextThreadLocal.remove();
    }


    @RequestMapping("/hello")
    public String index()
    {
        logger.info(String.format("%s.index [%s][%s]", HelloController.class, this, applicationContext));
        if (!contextThreadLocal.get().equals(applicationContext))
        {
            throw new RuntimeException("Bad applicationContext");
        }
        return String.format("Greetings from %s", HelloController.class);
    }

}