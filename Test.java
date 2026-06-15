<changeSet id="add_idx_t_sms_info_policy_no" author="stone">
    
    <preConditions onFail="MARK_RAN">
        <not>
            <indexExists indexName="idx_t_sms_info_policy_no" tableName="T_SMS_INFO"/>
        </not>
    </preConditions>

    <createIndex indexName="idx_t_sms_info_policy_no" tableName="T_SMS_INFO">
        <column name="POLICY_NO"/>
    </createIndex>
    
</changeSet>
