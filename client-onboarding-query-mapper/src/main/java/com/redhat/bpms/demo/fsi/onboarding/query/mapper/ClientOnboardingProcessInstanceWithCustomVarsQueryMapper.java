package com.redhat.bpms.demo.fsi.onboarding.query.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dashbuilder.dataset.DataSet;
import org.jbpm.kie.services.impl.query.mapper.AbstractQueryMapper;
import org.jbpm.services.api.model.ProcessInstanceWithVarsDesc;
import org.jbpm.services.api.query.QueryResultMapper;


public class ClientOnboardingProcessInstanceWithCustomVarsQueryMapper extends AbstractQueryMapper<ProcessInstanceWithVarsDesc> implements QueryResultMapper<List<ProcessInstanceWithVarsDesc>> {

	private static final long serialVersionUID = 5935133069234696715L;
    private Map<String, String> variablesMap = new HashMap<String, String>();
    
    /**
     * Dedicated for ServiceLoader to create instance, use <code>get()</code> method instead 
     */
    public ClientOnboardingProcessInstanceWithCustomVarsQueryMapper() {
        super();
    }
    
    public ClientOnboardingProcessInstanceWithCustomVarsQueryMapper(Map<String, String> variablesMap) {
        this.variablesMap = variablesMap;
    }

    public static ClientOnboardingProcessInstanceWithCustomVarsQueryMapper get(Map<String, String> variablesMap) {
        return new ClientOnboardingProcessInstanceWithCustomVarsQueryMapper(variablesMap);
    }

    @Override
    public List<ProcessInstanceWithVarsDesc> map(Object result) {
        if (result instanceof DataSet) {
            DataSet dataSetResult = (DataSet) result;
            List<ProcessInstanceWithVarsDesc> mappedResult = new ArrayList<ProcessInstanceWithVarsDesc>();
            
            Map<Long, ProcessInstanceWithVarsDesc> tmp = new HashMap<Long, ProcessInstanceWithVarsDesc>();
            
            if (dataSetResult != null) {
                
                for (int i = 0; i < dataSetResult.getRowCount(); i++) {
                    Long processInstanceId = getColumnLongValue(dataSetResult, COLUMN_PROCESSINSTANCEID, i);
                    ProcessInstanceWithVarsDesc pi = tmp.get(processInstanceId);
                    if (pi == null) {
                        pi = buildInstance(dataSetResult, i);                        
                        mappedResult.add(pi);
                        
                        tmp.put(processInstanceId, pi);
                    }
                    Map<String, Object> variables = readVariables(variablesMap, dataSetResult, i);                    
                    ((org.jbpm.kie.services.impl.model.ProcessInstanceWithVarsDesc)pi).setVariables(variables);
                }
            }
            tmp = null;
            return mappedResult;
        }
        
        throw new IllegalArgumentException("Unsupported result for mapping " + result);
    }
    
    protected ProcessInstanceWithVarsDesc buildInstance(DataSet dataSetResult, int index) {
        ProcessInstanceWithVarsDesc pi = new org.jbpm.kie.services.impl.model.ProcessInstanceWithVarsDesc(
                getColumnLongValue(dataSetResult, COLUMN_PROCESSINSTANCEID, index),
                getColumnStringValue(dataSetResult, COLUMN_PROCESSID, index),
                getColumnStringValue(dataSetResult, COLUMN_PROCESSNAME, index),
                getColumnStringValue(dataSetResult, COLUMN_PROCESSVERSION, index),
                getColumnIntValue(dataSetResult, COLUMN_STATUS, index),
                getColumnStringValue(dataSetResult, COLUMN_EXTERNALID, index),
                getColumnDateValue(dataSetResult, COLUMN_START, index),
                getColumnStringValue(dataSetResult, COLUMN_IDENTITY, index),
                getColumnStringValue(dataSetResult, COLUMN_PROCESSINSTANCEDESCRIPTION, index),
                getColumnStringValue(dataSetResult, COLUMN_CORRELATIONKEY, index), 
                getColumnLongValue(dataSetResult, COLUMN_PARENTPROCESSINSTANCEID, index)
                );
        return pi;
    }
    
    @Override
    public String getName() {
        return "ClientOnboardingProcessInstancesWithCustomVariables";
    }

    @Override
    public Class<?> getType() {
        return ProcessInstanceWithVarsDesc.class;
    }

    @Override
    public QueryResultMapper<List<ProcessInstanceWithVarsDesc>> forColumnMapping(Map<String, String> columnMapping) {
        return new ClientOnboardingProcessInstanceWithCustomVarsQueryMapper(columnMapping);
    }
}
