
            #Neo4j:
            #copy csv to neo4j import dir
            cp /Users/mark/program/proWorkspace/pyCharmSrc/GenGraph/result/result_2018915211915/*.csv /Users/mark/Documents/MasterLife/GraphDB/neo4j-community-3.4.6/import
            #execute Deploy.cypher to load csv to database instance
            cd /Users/mark/Documents/MasterLife/GraphDB/neo4j-community-3.4.6/bin
            ./neo4j-shell -file /Users/mark/program/proWorkspace/pyCharmSrc/GenGraph/result/result_2018915211915/Deploy_Neo4j.cypher
            #performance test
            ./neo4j-shell -file /Users/mark/program/proWorkspace/pyCharmSrc/GenGraph/result/result_2018915211915/Query_Neo4j.cypher
        