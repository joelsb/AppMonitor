package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Entities.PackageType;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.backendappmonitor.Exceptions.MyEntityNotFoundException;

import java.util.List;

@Stateless
public class PackageTypeBean {
    @PersistenceContext
    private EntityManager entityManager;

    public PackageType find(Long id) throws MyEntityNotFoundException {
        var packageType = entityManager.find(PackageType.class, id);
        if(packageType == null) {
            throw new MyEntityNotFoundException("PackageType (" + id + ") not found");
        }
        return packageType;
    }

    public List<PackageType> findAll() {
        return entityManager.createNamedQuery("getAllPackageTypes", PackageType.class).getResultList();
    }

    public PackageType create(String name) throws MyEntityExistsException {
        if(!entityManager.createNamedQuery("getPackageTypeByName", PackageType.class)
                .setParameter("name", name)
                .getResultList().isEmpty()) {
            throw new MyEntityExistsException("PackageType (" + name + ") already exists");
        }
        var packageType = new PackageType(name);
        entityManager.persist(packageType);
        return packageType;
    }

    public PackageType update(Long id, String name) throws MyEntityNotFoundException, MyEntityExistsException {
        var packageType = this.find(id);
        //if a package type with the same name already exists and its a different package type from the one passed with the id  then throw exception
        if(!entityManager.createNamedQuery("getPackageTypeByName", PackageType.class)
                .setParameter("name", name)
                .getResultList().isEmpty() && !packageType.getName().equals(name)) {
            throw new MyEntityExistsException("PackageType (" + name + ") already exists");
        }
        packageType.setName(name);
        return packageType;
    }


}
