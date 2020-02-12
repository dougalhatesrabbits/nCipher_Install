import com.log.*;
import com.platform.*;
import com.file.*;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.IOException;
import java.util.logging.Logger;

public class Test {

        // Always use the classname, this way you can refactor
        private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        //private final static Logger LOGGER = Logger.getLogger(Install.class.getName());

        public static void main(String[] args) throws IOException {


            String _level_ = "INFO";

            InstallLogger log = new InstallLogger();
            log.setup(_level_);

            LOGGER.info("New instance of Install started");


            log.close();
            System.exit(0);
        }

}
