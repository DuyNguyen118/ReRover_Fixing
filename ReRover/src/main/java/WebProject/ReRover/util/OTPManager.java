// OTPManager.java
package WebProject.ReRover.util;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OTPManager {
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes
    private static final Map<String, OTPData> otpStorage = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Random random = new Random();

    static {
        // Schedule a task to clean up expired OTPs periodically
        scheduler.scheduleAtFixedRate(OTPManager::cleanupExpiredOtps, 1, 1, TimeUnit.HOURS);
    }

    public static String generateOTP(String email) {
        // Generate a 6-digit OTP
        int otp = 100000 + random.nextInt(900000);
        String otpString = String.valueOf(otp);
        
        // Store the OTP with its expiration time
        otpStorage.put(email, new OTPData(otpString, System.currentTimeMillis() + OTP_VALID_DURATION));
        
        return otpString;
    }

    public static boolean verifyOTP(String email, String otp) {
        OTPData otpData = otpStorage.get(email);
        if (otpData == null || otpData.isExpired()) {
            return false;
        }
        
        // Remove the OTP after verification
        otpStorage.remove(email);
        return otpData.getOtp().equals(otp);
    }

    private static void cleanupExpiredOtps() {
        long currentTime = System.currentTimeMillis();
        otpStorage.entrySet().removeIf(entry -> entry.getValue().isExpired(currentTime));
    }

    private static class OTPData {
        private final String otp;
        private final long expiryTime;

        public OTPData(String otp, long expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public boolean isExpired() {
            return isExpired(System.currentTimeMillis());
        }

        public boolean isExpired(long currentTime) {
            return currentTime > expiryTime;
        }
    }
}