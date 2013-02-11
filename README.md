restq
=====
RestQ is an experimental message queue that I'm trying out. This is more of a message bus than a message queue. It takes the idea from distributed data grid solutions and mongo replication and attempts to store all the messages in memory across several nodes. Every node in the cluster is a master and we can configure 'n' slaves for every node, so when a master goes down, one of the slaves can become a master  
