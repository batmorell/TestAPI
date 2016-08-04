package org.api.service;

import org.api.utils.TechniqueException;

public interface ResponseSupplierWrapper {

    public Object get() throws TechniqueException;
}