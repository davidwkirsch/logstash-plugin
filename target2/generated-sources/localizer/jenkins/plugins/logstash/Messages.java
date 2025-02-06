// CHECKSTYLE:OFF

package jenkins.plugins.logstash;

import org.jvnet.localizer.Localizable;
import org.jvnet.localizer.ResourceBundleHolder;
import org.kohsuke.accmod.Restricted;


/**
 * Generated localization support class.
 * 
 */
@SuppressWarnings({
    "",
    "PMD",
    "all"
})
@Restricted(org.kohsuke.accmod.restrictions.NoExternalUse.class)
public class Messages {

    /**
     * The resource bundle reference
     * 
     */
    private final static ResourceBundleHolder holder = ResourceBundleHolder.get(Messages.class);

    /**
     * Key {@code ValueIsInt}: {@code Value must be an integer}.
     * 
     * @return
     *     {@code Value must be an integer}
     */
    public static String ValueIsInt() {
        return holder.format("ValueIsInt");
    }

    /**
     * Key {@code ValueIsInt}: {@code Value must be an integer}.
     * 
     * @return
     *     {@code Value must be an integer}
     */
    public static Localizable _ValueIsInt() {
        return new Localizable(holder, "ValueIsInt");
    }

    /**
     * Key {@code ValueIsRequired}: {@code Value is required}.
     * 
     * @return
     *     {@code Value is required}
     */
    public static String ValueIsRequired() {
        return holder.format("ValueIsRequired");
    }

    /**
     * Key {@code ValueIsRequired}: {@code Value is required}.
     * 
     * @return
     *     {@code Value is required}
     */
    public static Localizable _ValueIsRequired() {
        return new Localizable(holder, "ValueIsRequired");
    }

    /**
     * Key {@code DisplayName}: {@code Send console log to Logstash}.
     * 
     * @return
     *     {@code Send console log to Logstash}
     */
    public static String DisplayName() {
        return holder.format("DisplayName");
    }

    /**
     * Key {@code DisplayName}: {@code Send console log to Logstash}.
     * 
     * @return
     *     {@code Send console log to Logstash}
     */
    public static Localizable _DisplayName() {
        return new Localizable(holder, "DisplayName");
    }

    /**
     * Key {@code PleaseProvideHost}: {@code Please set a valid host name}.
     * 
     * @return
     *     {@code Please set a valid host name}
     */
    public static String PleaseProvideHost() {
        return holder.format("PleaseProvideHost");
    }

    /**
     * Key {@code PleaseProvideHost}: {@code Please set a valid host name}.
     * 
     * @return
     *     {@code Please set a valid host name}
     */
    public static Localizable _PleaseProvideHost() {
        return new Localizable(holder, "PleaseProvideHost");
    }

    /**
     * Key {@code ProvideValidMimeType}: {@code Please provide a valid mime
     * type}.
     * 
     * @return
     *     {@code Please provide a valid mime type}
     */
    public static String ProvideValidMimeType() {
        return holder.format("ProvideValidMimeType");
    }

    /**
     * Key {@code ProvideValidMimeType}: {@code Please provide a valid mime
     * type}.
     * 
     * @return
     *     {@code Please provide a valid mime type}
     */
    public static Localizable _ProvideValidMimeType() {
        return new Localizable(holder, "ProvideValidMimeType");
    }

}
