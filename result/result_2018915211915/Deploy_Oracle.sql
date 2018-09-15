
        delete from T_ITEM_TEST;
        delete from T_RELATION_TEST;

        select 'Start time: ', to_char(current_timestamp,'yyyy-mm-dd hh24:mi:ss.ff6') from dual;

        @/Users/mark/program/proWorkspace/pyCharmSrc/GenGraph\result\g1_new_nodes_as_files_item.sql
        @/Users/mark/program/proWorkspace/pyCharmSrc/GenGraph\result\g1_new_nodes_as_files_relation.sql

        select 'End time: ', to_char(current_timestamp,'yyyy-mm-dd hh24:mi:ss.ff6') from dual;

        quit;
                