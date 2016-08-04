package org.service;

import org.utils.TechniqueException;

public interface ResponseSupplierWrapper {

    public Object get() throws TechniqueException;
}