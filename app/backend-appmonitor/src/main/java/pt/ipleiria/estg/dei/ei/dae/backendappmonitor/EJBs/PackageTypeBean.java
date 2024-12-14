package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;


import jakarta.ejb.Stateless;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.PackageType;

@Stateless
public class PackageTypeBean
{
    @PersistenceContext
    private EntityManager entityManager;

    public PackageType create(String name)
    {
        var packageType = new PackageType(name);
        entityManager.persist(packageType);
        return packageType;
    }

}
