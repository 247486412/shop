package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016101200667148";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCEPDazfLIc9DBiJU0xrQL/HEaYDEZCG2bRBlJybZFft1yCEL2i3eiLORpJLik1/+EoRWegY0l71L1D+0sPBx+mmBVgHGqH0hwycmh6Be9sXt4YgUaMZhn8K9inZfJMdVPrnzmAnZJzcn53N4f4zow62Ssbt5eJe2tAB5uW7nB+8EQsc+wpee0J4zXXDwoQnlZSqfAPq/ozO4HjLz2kAoWESlapI3mcakZpG+gSnkpGQEZagxOAniqlCBQ8JMUpABtG7q0kwILdh515ir2UB3mrH9yaSwfMTFL3jIQp/Mc160oJ8EPnCc9ZJW3e8QnSwOjAid1ojpjeg9VcJFheGmHHAgMBAAECggEAECuEzOH8rcm2eOnyzJZJfYSppUIq/T54jirlvhRj6H51k3oUlmvoWc37xuMcKvxFPjVufJBnlfrnwf5iqU3Hswl546zV4hHxXMHcwL8lDe1z3y8/vz9lAg8iiSXQQUU/o9pun6mlFPT1PgLOX8ByKWgQ+2yx3R/4d3X2srgpjXwKqg0zQWaXJ2aDrv2F354riGnEd9uWhW+nei59uDVTXlHR5q/zWumdHqGEMbejWWMVhKpM2Ii3/wwIpqWv2VTfBgC1BUQXQ8Y8MFr6IcTyFH2e8ZQRPm8Cf1O6Z1Lsyv9YAfRaGxyOroFapMWD8xBmCD5GzK0ZSP1J21cV/UPSoQKBgQDPE8NWwYutVfybg+yApoUWaC8QAJ2FvjcU9M8/f1uD+bZEyj8IhbKKC6A9wV3m1qp3CwRT69DXlpxLJV5We3Or0NGZs1dkDv6Dg4xLum8Ys/RCSCxA+JZBdgouldTw2dzJMMHwQ6EJbOj603cm5qHhsXF6diw0cb6Zx/5Rf8iT7wKBgQCjee6TyiG5mgljJCN3rlRv8PR9fcXdpfM13WG4i+0a2jWCGyTGm4KkyFkC9G1lQYIKGH5AvI3uLzHZOx3hIg7Bh6CnJ0PK7Qa070/zU9fCu4IgINSDJ0TUKZd9aU7eii331KIncd/uZNscaI5fGjzDs0cu6VDCqDtmG7BuGb3XqQKBgD70/j2hKGsxqlRJvpy6g9n1IOHXngyscSiw6VWfzd4JDI5LgvB/XXG06Sn9YSQufbMlqVIubtcs1iXOnajjbZ5+JUt6iAZGvMCVPRFFBEPZaljAgpvaOCx8y8mwCgaT5hlQ9pWqqgooj4i6xA4SroLkpOCbjwL+1fkBIeP7WqsHAoGAL4+a/L5zH2eUUZbDBt/eSw8kYppEBd9xKfu1oZZ8LjLMHIU/JtU5pi1v0C9C4JG1frBvkvAIrwJmE1hisgxRlLab4+XFkHVzXRK/BQEgpdQd4LEMgvfay4sqOt93hcFX7GGdp7I2v0nygYpd2YdtpFY1YI2a5HdDiMqHgxDULbkCgYAIFfNNnWj5LCAxVRXeZ4oB+H8p4+PUAXlUaQ43M8BJiS9nTavtOmChlHU51ByMDFTQvTvrvAIs4yy8MFprCxuAWUaUNZ9NqYyuGWIVNo68bH+JTlv6ZpXWZ5CL9wkRbuQCco3IZycUwP7wo4Da4xCVwr9vK2lBNLU0amd3n9BW5Q==";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzQS+mllOX3C670n1wqRirbvetWincUAOlI5Pf648Iv2+rxARNXWtSzimz+KGTp8znBYonPWCN1vzMS80c9ELxv6xXV2R3RtOhwacdytQBRHv5bBF+8YY7ROXFuYGy7zDTts0ffvCykL6182B1u5FZ7T8khmv1xvCOqjsnJaEtrbY6DU395Ybkx3a3C/CWH3TcVyhZtap40WN4LFIVCa3OTLqepNECKoesTkfpwDf+v+08gPwf3BDfcq74BrJUAnzv91pnpZ1DfdvHnJeY1dRlNodhX0f8ehNbz+pB/ZHTSIjdFn8BKEAOfC5+HeG2OkZE7EqfgKqeDB+uUAZy48PuQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://www.hutao.fun/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://www.hutao.fun/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关,测试环境网关域名多了dev
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "E:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

