-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#HtmlUnit 无界面流量
-keep public class com.gargoylesoftware.**
-keep class com.gargoylesoftware.**{ *; }
-keep public class java.**
-keep public class javax.**
-keep public class netscape.javascript.**
-keep public class org.w3c.dom.**
-keep public class sun.awt.**
-keep public class xyz.nulldev.huandroid.**
-keep class org.apache.** {*;}


-keep class org.apache.xerces.dom.DocumentImpl.** {*;}
-dontwarn org.apache.xerces.dom.DocumentImpl
-keep class net.sourceforge.htmlunit.** {*;}
-dontwarn net.sourceforge.htmlunit.**
-keep class org.apache.bcel.verifier.** {*;}
-dontwarn org.apache.bcel.verifier.**
-keep class org.apache.commons.logging.** {*;}
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.regexp.**
-dontwarn org.apache.xalan.**
-dontwarn org.apache.xerces.**
-keep class org.eclipse.jetty.** {*;}
-dontwarn org.eclipse.jetty.**
-keep class org.xmlpull.** {*;}
-dontwarn org.xmlpull.**
-dontwarn javax.**
-keep class android.util.Xml.** {*;}
-dontwarn android.util.Xml
-dontwarn org.apache.**