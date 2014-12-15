/*
 * Class that stores basic information about an ASpace repository
 */

package areports.model;

/**
 *
 * @author nathan
 */
public class RepositoryInfo {
    private int repositoryId = -1;
    private String repositoryName;
    
    /**
     * The default constructor
     * @param repositoryId
     * @param repositoryName 
     */
    public RepositoryInfo(int repositoryId, String repositoryName) {
        this.repositoryId = repositoryId;
        this.repositoryName = repositoryName;
    }
    
    /**
     * Method to return repository Id
     * 
     * @return 
     */
    public Integer getRepositoryId() {
        return repositoryId;
    }
    
    @Override
    public String toString() {
        return repositoryName;
    }
}
