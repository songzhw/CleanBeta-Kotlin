package cn.six.payx.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CertUtil {
    public static byte[] getSign(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm
                .getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();

        while (iter.hasNext()) {
            PackageInfo info = iter.next();
            String packageName = info.packageName;
            //按包名 取签名
            if (packageName.equals("cn.six.payx")) {
                return info.signatures[0].toByteArray();

            }
        }
        return null;
    }

    public static String getPublicKey(byte[] signature) {
        String sign = "";
        try {
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String ss = subString(pubKey);
            ss = ss.replace(",", "");
            ss = ss.toLowerCase();
            int aa = ss.indexOf("modulus");
            int bb = ss.indexOf("publicexponent");
            sign = ss.substring(aa + 8, bb);
        } catch (CertificateException e) {
            Log.e("szw", e.getMessage(), e);
        }
        return sign;
    }

    public static String subString(String sub) {
        Pattern pp = Pattern.compile("\\s*|\t|\r|\n");
        Matcher mm = pp.matcher(sub);
        return mm.replaceAll("");
    }
}
