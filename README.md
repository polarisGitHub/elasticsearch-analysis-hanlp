# elasticsearch-analysis-hanlp

## HanLP Analyzer for ElasticSearch

- 该仓库代码都来自[KennFalcon/elasticsearch-analysis-hanlp](https://github.com/KennFalcon/elasticsearch-analysis-hanlp)

## CHANGE LOG

1. 将gradle打包改为了maven打包，并适配elasticsearch 7.17.16
2. 将`PathUtils`的包调整为`org.elasticsearch.core.PathUtils`，以适配高版本es
3. `AnalysisHanLPPlugin`NLP和CRF模型文件默认路径修改为`plugins/analysis-hanlp/data`
4. 权限最小化，这里作者只测试了基本分词功能，没有测试字典热更新，需要字典热更新的可能需要添加更多权限

## NOTE

- 由于hanlp需要读取，写入和删除模型，在高版本ES使用该插件，需要在`${ES_HOME}/jdk/conf/security/java.policy`中添加以下内容`permission java.io.FilePermission "-","read,write,delete";`