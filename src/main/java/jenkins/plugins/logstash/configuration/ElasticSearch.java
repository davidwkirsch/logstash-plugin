package jenkins.plugins.logstash.configuration;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCertificateCredentials;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import hudson.Extension;
import hudson.model.Item;
import hudson.security.ACL;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import jenkins.model.Jenkins;
import jenkins.plugins.logstash.Messages;
import jenkins.plugins.logstash.persistence.ElasticSearchDao;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElasticSearch extends LogstashIndexer<ElasticSearchDao>
{
  private static final Logger LOGGER = Logger.getLogger(ElasticSearch.class.getName());

  private String username;
  private Secret password;
  private URI uri;
  private String mimeType;
  private String customServerCertificateId;

  @DataBoundConstructor
  public ElasticSearch()
  {
  }

  public URI getUri()
  {
    return uri;
  }

  @Override
 public void validate() throws MimeTypeParseException {
    new MimeType(this.mimeType);
  }

  /*
   * We use URL for the setter as stapler can autoconvert a string to a URL but not to a URI
   */
  @DataBoundSetter
  public void setUri(URL url) throws URISyntaxException
  {
    this.uri = url.toURI();
  }

  public void setUri(URI uri)
  {
    this.uri = uri;
  }

  public String getUsername()
  {
    return username;
  }

  @DataBoundSetter
  public void setUsername(String username)
  {
    this.username = username;
  }

  public Secret getPassword()
  {
    return password;
  }

  @DataBoundSetter
  public void setPassword(Secret password)
  {
    this.password = password;
  }

  @DataBoundSetter
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public String getMimeType() {
    return mimeType;
  }

  @DataBoundSetter
  public void setCustomServerCertificateId(String customServerCertificateId)
  {
    this.customServerCertificateId = customServerCertificateId;
  }

  public String getCustomServerCertificateId()
  {
    return customServerCertificateId;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    if (getClass() != obj.getClass())
      return false;
    ElasticSearch other = (ElasticSearch) obj;
    if (!Secret.toString(password).equals(Secret.toString(other.getPassword())))
    {
      return false;
    }
    if (uri == null)
    {
      if (other.uri != null)
        return false;
    }
    else if (!uri.equals(other.uri))
    {
      return false;
    }
    if (username == null)
    {
      if (other.username != null)
        return false;
    }
    else if (!username.equals(other.username))
    {
      return false;
    }
    else if (!mimeType.equals(other.mimeType))
    {
      return false;
    }

    if (this.customServerCertificateId == null)
    {
      if (other.customServerCertificateId != null)
        return false;
    }
    else if (!this.customServerCertificateId.equals(other.customServerCertificateId))
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((uri == null) ? 0 : uri.hashCode());
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    result = prime * result + Secret.toString(password).hashCode();
    return result;
  }

  @Override
  public ElasticSearchDao createIndexerInstance()
  {
    ElasticSearchDao esDao = new ElasticSearchDao(getUri(), username, Secret.toString(password));

    esDao.setMimeType(getMimeType());
    if (!StringUtils.isBlank(customServerCertificateId)) {
      try {
          StandardCertificateCredentials certificateCredentials = getCredentials(customServerCertificateId);
          if (certificateCredentials != null) {
            esDao.setCustomKeyStore(certificateCredentials.getKeyStore(),
                Secret.toString(certificateCredentials.getPassword()));
          }
      } catch (KeyStoreException | CertificateException |
               NoSuchAlgorithmException | IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
      }
    }
    return esDao;
  }

  private StandardCertificateCredentials getCredentials(String credentials)
  {
    return (StandardCertificateCredentials) CredentialsMatchers.firstOrNull(
        CredentialsProvider.lookupCredentials(StandardCertificateCredentials.class,
            Jenkins.get(), ACL.SYSTEM, Collections.emptyList()),
        CredentialsMatchers.withId(credentials)
    );
  }

  @Extension
  @Symbol("elasticSearch")
  public static class ElasticSearchDescriptor extends LogstashIndexerDescriptor
  {
    @Override
    public String getDisplayName()
    {
      return "Elastic Search";
    }

    @Override
    public int getDefaultPort()
    {
      return 0;
    }

    public ListBoxModel doFillCustomServerCertificateIdItems(
        @AncestorInPath Item item,
        @QueryParameter String customServerCertificateId)
    {
      return new StandardListBoxModel().withEmptySelection()
          .withMatching( //
              CredentialsMatchers.anyOf(
                  CredentialsMatchers.instanceOf(StandardCertificateCredentials.class)
              ),
              CredentialsProvider.lookupCredentials(StandardCredentials.class,
                  Jenkins.get(),
                  ACL.SYSTEM,
                  Collections.emptyList()
              )
          );
    }

    public FormValidation doCheckUrl(@QueryParameter("value") String value)
    {
      if (StringUtils.isBlank(value))
      {
        return FormValidation.warning(Messages.PleaseProvideHost());
      }
      try
      {
        URL url = new URL(value);

        if (url.getUserInfo() != null)
        {
          return FormValidation.error("Please specify user and password not as part of the url.");
        }

        if(StringUtils.isBlank(url.getPath()) || url.getPath().trim().matches("^\\/+$")) {
          return FormValidation.warning("Elastic Search requires a key to be able to index the logs.");
        }

        url.toURI();
      }
      catch (MalformedURLException | URISyntaxException e)
      {
        return FormValidation.error(e.getMessage());
      }
      return FormValidation.ok();
    }
    public FormValidation doCheckMimeType(@QueryParameter("value") String value) {
      if (StringUtils.isBlank(value)) {
        return FormValidation.error(Messages.ValueIsRequired());
      }
      try {
        //This is simply to check validity of the given mimeType
        new MimeType(value);
      } catch (MimeTypeParseException e) {
        return FormValidation.error(Messages.ProvideValidMimeType());
      }
      return FormValidation.ok();
    }
  }
}
