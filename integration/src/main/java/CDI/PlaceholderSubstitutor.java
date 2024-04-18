package CDI;


import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;



@ApplicationScoped
public class PlaceholderSubstitutor {


    Logger logger = LoggerFactory.getLogger(PlaceholderSubstitutor.class);

    /**
     * Substitutes placeholders in the input string with values from the valuesMap
     * Checks if all placeholders are replaced
     * TODO: Would static be better?
     *
     *
     * @param input     the input string with placeholders
     * @param valuesMap the map with the placeholder as key and the value as value
     * @return the input string with the placeholders replaced by the values
     */
    public String substituteAll(String input, HashMap<String, String> valuesMap) throws Exception {

        StringSubstitutor substitutor = new StringSubstitutor(valuesMap);
        String output = substitutor.replace(input);
        if(output.contains("${")){
            logger.error("Value of output: {}", output);
            throw new Exception("Not all placeholders are replaced!");
        }
        return output;
    }
}
