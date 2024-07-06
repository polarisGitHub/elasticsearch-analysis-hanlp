package org.elasticsearch.plugin.analysis.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.utility.Predefine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.core.PathUtils;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.AnalyzerProvider;
import org.elasticsearch.index.analysis.HanLPAnalyzerProvider;
import org.elasticsearch.index.analysis.HanLPTokenizerFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description: Hanlp分词插件
 * Author: Kenn
 * Create: 2018-12-14 15:10
 */
public class AnalysisHanLPPlugin extends Plugin implements AnalysisPlugin {
    private static final Logger logger = LogManager.getLogger(AnalysisHanLPPlugin.class);

    private final String NLP_CWS = "plugins/analysis-hanlp/data/model/perceptron/large/cws.bin";
    private final String CRF_CWS = "plugins/analysis-hanlp/data/model/crf/pku199801/cws.txt.bin";
    public static String PLUGIN_NAME = "analysis-hanlp";

    /**
     * Hanlp配置文件名
     */
    private static final String CONFIG_FILE_NAME = "hanlp.properties";

    public AnalysisHanLPPlugin(Settings settings) {
        String home = null;
        if (Environment.PATH_HOME_SETTING.exists(settings)) {
            home = Environment.PATH_HOME_SETTING.get(settings);
        }
        if (home == null) {
            throw new IllegalStateException(Environment.PATH_HOME_SETTING.getKey() + " is not configured");
        } else {
            Path configDir = PathUtils.get(home, "config", AnalysisHanLPPlugin.PLUGIN_NAME);
            Predefine.HANLP_PROPERTIES_PATH = configDir.resolve(CONFIG_FILE_NAME).toString();
            logger.debug("hanlp properties path: {}", Predefine.HANLP_PROPERTIES_PATH);
        }
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> extra = new HashMap<>();

        extra.put("hanlp", HanLPTokenizerFactory::getHanLPTokenizerFactory);
        extra.put("hanlp_standard", HanLPTokenizerFactory::getHanLPStandardTokenizerFactory);
        extra.put("hanlp_index", HanLPTokenizerFactory::getHanLPIndexTokenizerFactory);

        Path nlpCwsPath = Paths.get(AccessController.doPrivileged((PrivilegedAction<String>) () -> NLP_CWS)).toAbsolutePath();
        if (FileSystemUtils.exists(nlpCwsPath)) {
            extra.put("hanlp_nlp", HanLPTokenizerFactory::getHanLPNLPTokenizerFactory);
        } else {
            logger.warn("can not find perceptron cws model from [{}], you can not use tokenizer [hanlp_nlp]", nlpCwsPath);
        }

        Path crfCwsPath = Paths.get(AccessController.doPrivileged((PrivilegedAction<String>) () -> CRF_CWS)).toAbsolutePath();
        if (FileSystemUtils.exists(crfCwsPath)) {
            extra.put("hanlp_crf", HanLPTokenizerFactory::getHanLPCRFTokenizerFactory);
        } else {
            logger.warn("can not find crf cws model from [{}], you can not use tokenizer [hanlp_crf]", crfCwsPath);
        }
        extra.put("hanlp_n_short", HanLPTokenizerFactory::getHanLPNShortTokenizerFactory);
        extra.put("hanlp_dijkstra", HanLPTokenizerFactory::getHanLPDijkstraTokenizerFactory);
        extra.put("hanlp_speed", HanLPTokenizerFactory::getHanLPSpeedTokenizerFactory);

        return extra;
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> extra = new HashMap<>();

        extra.put("hanlp", HanLPAnalyzerProvider::getHanLPAnalyzerProvider);
        extra.put("hanlp_standard", HanLPAnalyzerProvider::getHanLPStandardAnalyzerProvider);
        extra.put("hanlp_index", HanLPAnalyzerProvider::getHanLPIndexAnalyzerProvider);


        Path nlpCwsPath = Paths.get(AccessController.doPrivileged((PrivilegedAction<String>) () -> NLP_CWS)).toAbsolutePath();
        if (FileSystemUtils.exists(nlpCwsPath)) {
            extra.put("hanlp_nlp", HanLPAnalyzerProvider::getHanLPNLPAnalyzerProvider);
        } else {
            logger.warn("can not find perceptron cws model from [{}], you can not use analyzer [hanlp_nlp]", nlpCwsPath);
        }

        Path crfCwsPath = Paths.get(AccessController.doPrivileged((PrivilegedAction<String>) () -> CRF_CWS)).toAbsolutePath();
        if (FileSystemUtils.exists(crfCwsPath)) {
            extra.put("hanlp_crf", HanLPAnalyzerProvider::getHanLPCRFAnalyzerProvider);
        } else {
            logger.warn("can not find crf cws model from [{}], you can not use analyzer [hanlp_crf]", crfCwsPath);
        }
        extra.put("hanlp_n_short", HanLPAnalyzerProvider::getHanLPNShortAnalyzerProvider);
        extra.put("hanlp_dijkstra", HanLPAnalyzerProvider::getHanLPDijkstraAnalyzerProvider);
        extra.put("hanlp_speed", HanLPAnalyzerProvider::getHanLPSpeedAnalyzerProvider);

        return extra;
    }
}
