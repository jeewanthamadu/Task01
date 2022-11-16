package lk.directpay.task.services;


import lk.directpay.task.entity.AppUser;
import lk.directpay.task.entity.AppUserActivity;
import lk.directpay.task.enums.ActivityLogAction;
import lk.directpay.task.repository.AppUserActivityRepository;
import lk.directpay.task.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class ActivityLogService {
    private final AppUserActivityRepository activityRepository;
    private final UserRepository userRepository;

    @Autowired
    public ActivityLogService(AppUserActivityRepository activityRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    public AppUserActivity log(AppUser user, String action, String requestedPath,
                               String entity, String description) {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.
                        currentRequestAttributes()).
                        getRequest();

        AppUserActivity appUserActivity = new AppUserActivity();
        appUserActivity.setUsername(user.getUsername());
        appUserActivity.setNic(user.getNic());
        appUserActivity.setMobile(user.getPhoneNumber());
        appUserActivity.setEmail(user.getEmail());
        appUserActivity.setAction(action);
        appUserActivity.setEntity(entity);
        appUserActivity.setApiEndpoint(requestedPath);
        appUserActivity.setDescription(description);

        appUserActivity.setDeviceId(user.getDeviceId());
        appUserActivity.setAppVersion(user.getAppVersion());
        appUserActivity.setRequestId(MDC.get("req-id"));
        appUserActivity.setIpAddress(request.getRemoteAddr());
        activityRepository.save(appUserActivity);

        return appUserActivity;
    }

    public AppUserActivity logApiCall(String action, String requestedPath,
                                      String apiRequest, String description) {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.
                        currentRequestAttributes()).
                        getRequest();

        String activityReference = Objects.requireNonNull(RequestContextHolder.currentRequestAttributes().getAttribute("activity_ref", RequestAttributes.SCOPE_REQUEST)).toString();

        AppUserActivity appUserActivity = new AppUserActivity();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)){
            String username = auth.getName();
            if (!StringUtils.isEmpty(username)){
                AppUser user = userRepository.findOneByUsername(username);
                if (user != null){
                    appUserActivity.setNic(user.getNic());
                    appUserActivity.setMobile(user.getPhoneNumber());
                    appUserActivity.setEmail(user.getEmail());
                    appUserActivity.setUsername(user.getUsername());
                    appUserActivity.setDeviceId(user.getDeviceId());
                    appUserActivity.setAppVersion(user.getAppVersion());
                }
            }
        }
        appUserActivity.setAction(action);
        appUserActivity.setApiRequest(apiRequest.length() > 1000 ? apiRequest.substring(0, 800) : apiRequest);
        appUserActivity.setApiEndpoint(requestedPath);
        appUserActivity.setDescription(description);
        appUserActivity.setUserReference(activityReference);
        appUserActivity.setRequestId(MDC.get("req-id"));
        appUserActivity.setIpAddress(request.getRemoteAddr());
        activityRepository.save(appUserActivity);

        return appUserActivity;
    }

    public void logSuccess(AppUserActivity appUserActivity) {
        appUserActivity.setStatus(AppUserActivity.SUCCESS);
        activityRepository.save(appUserActivity);
    }

    public void logFailed(AppUserActivity appUserActivity) {
        appUserActivity.setStatus(AppUserActivity.FAILED);
        activityRepository.save(appUserActivity);
    }

    public void saveChanges(AppUserActivity appUserActivity){
        activityRepository.save(appUserActivity);
    }

    public void saveReqLogActivity(AppUserActivity appUserActivity){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)){
            String username = auth.getName();
            if (!StringUtils.isEmpty(username)){
                AppUser user = userRepository.findOneByUsername(username);
                if (user != null){
                    appUserActivity.setNic(user.getNic());
                    appUserActivity.setMobile(user.getPhoneNumber());
                    appUserActivity.setEmail(user.getEmail());
                    appUserActivity.setUsername(user.getUsername());
                    appUserActivity.setDeviceId(user.getDeviceId());
                    appUserActivity.setAppVersion(user.getAppVersion());
                }
            }
        }
        if (appUserActivity.getApiRequest().length() > 1000){
            appUserActivity.setApiRequest(appUserActivity.getApiRequest().substring(0, 800));
        }
        activityRepository.save(appUserActivity);
    }


    public String getActionFromPath(String requestPath){
        switch (requestPath){
            case "/api/account/link":
                return ActivityLogAction.LINK_ACCOUNTS_TO_DEVICE.name();
            case "/api/account/add":
                return ActivityLogAction.ADD_ACCOUNT.name();
            case "/api/account/verify":
                return ActivityLogAction.VERIFY_ACCOUNT.name();
            case "/api/account/signature":
                return ActivityLogAction.ADD_ACCOUNT_SIGNATURE.name();
            case "/api/account/delete":
                return ActivityLogAction.ACCOUNT_DELETE.name();
            case "/api/account/all":
                return ActivityLogAction.PAYMENT_METHOD_LIST.name();
            case "/api/account/statements":
                return ActivityLogAction.ACCOUNT_STATEMENT.name();
            case "/api/account/statement/pdf":
                return ActivityLogAction.ACCOUNT_STATEMENT_PDF.name();
            case "/api/account/transactions":
                return ActivityLogAction.ACCOUNT_TRANSACTIONS.name();
            case "/api/account/balance":
                return ActivityLogAction.ACCOUNT_BALANCE.name();

            case "/api/app/config/check/update":
                return ActivityLogAction.CHECK_UPDATES.name();
            case "/api/app/config/termsAndConditions":
                return ActivityLogAction.GET_TERMS_AND_CONDITIONS.name();
            case "/api/app/contact/info":
                return ActivityLogAction.GET_CONTACT_INFO.name();
            case "/api/app/banner/all":
                return ActivityLogAction.GET_BANNERS.name();

            case "/api/bank/all":
                return ActivityLogAction.GET_ALL_BANKS.name();
            case "/api/bank/ceft":
                return ActivityLogAction.GET_CEFT_BANKS.name();
            case "/api/bank/credit-card":
                return ActivityLogAction.GET_CREDIT_CARD_ENABLED_BANKS.name();
            case "/api/bank/branch":
                return ActivityLogAction.GET_SLIPS_ENABLED_BANK_BRANCHES.name();

            case "/api/biller/all":
                return ActivityLogAction.BILLER_LIST.name();
            case "/api/biller/saved/frequently":
                return ActivityLogAction.FREQUENTLY_SAVED_BILLERS.name();
            case "/api/biller/recent/transactions":
                return ActivityLogAction.BILLERS_RECENT_TRANS_ACCOUNT.name();
            case "/api/biller/recent/transactions/card":
                return ActivityLogAction.BILLERS_CARD_RECENT_TRANS.name();
            case "/api/biller/pay":
                return ActivityLogAction.BILL_PAYMENT_BY_ACCOUNT.name();
            case "/api/biller/save":
                return ActivityLogAction.SAVE_BILLER.name();
            case "/api/biller/find":
                return ActivityLogAction.GET_BILLER.name();

            case "/api/location/list":
                return ActivityLogAction.GET_LOCATIONS_LIST.name();

            case "/api/card/other/create-session":
                return ActivityLogAction.CREATE_SESSION.name();
            case "/api/card/other/check-3ds":
                return ActivityLogAction.CHECK_3DS.name();
            case "/api/card/other/add":
                return ActivityLogAction.TOKENIZE_CARD.name();
            case "/api/card/delete":
                return ActivityLogAction.DELETE_USER_CARD.name();
            case "/api/card/add":
                return ActivityLogAction.ADD_USER_CARD.name();
            case "/api/card/verify":
                return ActivityLogAction.VERIFY_CARD.name();
            case "/api/card/list":
                return ActivityLogAction.CARD_LIST.name();
            case "/api/card/report/lost/stolen":
                return ActivityLogAction.REPORT_LOST_STOLEN_CARD.name();
            case "/api/card/statement/list":
                return ActivityLogAction.LOAD_CARD_STATEMENTS_LIST.name();
            case "/api/card/statement/details/page":
                return ActivityLogAction.LOAD_CARD_STATEMENT_DETAILS.name();
            case "/api/card/statement/details":
                return ActivityLogAction.LOAD_CARD_STATEMENT_DETAILS_PDF.name();
            case "/api/card/balance":
                return ActivityLogAction.GET_CARD_BALANCE.name();
            case "/api/card/transactions":
                return ActivityLogAction.GET_CARD_RECENT_TRANSACTIONS.name();

            case "/api/city/list":
                return ActivityLogAction.GET_CITY_LIST.name();

            case "/api/card/payment":
                return ActivityLogAction.CARD_PAYMENT.name();
            case "/api/card/bill/pay":
                return ActivityLogAction.BILL_PAYMENT_BY_CARD.name();

            case "/api/fund/transfer":
                return ActivityLogAction.FUND_TRANSFER.name();

            case "/api/government/payment":
                return ActivityLogAction.INITIATE_GOVERNMENT_PAYMENT.name();

            case "/api/message/subjects":
                return ActivityLogAction.SUBJECTS.name();
            case "/api/message/all":
                return ActivityLogAction.ALL_MESSAGES.name();
            case "/api/message/send":
                return ActivityLogAction.SEND_MESSAGE.name();

            case "/api/notification/all":
                return ActivityLogAction.ALL_NOTIFICATIONS.name();
            case "/api/notification/send":
                return ActivityLogAction.SEND_NOTIFICATION.name();

            case "/api/promotion/categories":
                return ActivityLogAction.PROMOTION_CATEGORIES.name();
            case "/api/promotion/all":
                return ActivityLogAction.ALL_PROMOTIONS.name();

            case "/api/issue/report":
                return ActivityLogAction.REPORT_ISSUE.name();

            case "/api/saved/biller":
                return ActivityLogAction.SAVED_BILLER_LIST.name();
            case "/api/saved/payee":
                return ActivityLogAction.SAVED_PAYEE_LIST.name();
            case "/api/saved/credit-card":
                return ActivityLogAction.SAVED_CREDIT_CARDS_LIST.name();
            case "/api/saved/payee/delete":
                return ActivityLogAction.DELETE_SAVED_PAYEE.name();
            case "/api/saved/biller/delete":
                return ActivityLogAction.DELETE_SAVED_BILLER.name();
            case "/api/saved/credit-card/delete":
                return ActivityLogAction.DELETE_SAVED_CREDIT_CARD.name();
            case "/api/saved/get/beneficiary":
                return ActivityLogAction.GET_BENEFICIARY.name();
            case "/api/saved/payee/send/otp":
                return ActivityLogAction.PAYEE_SEND_OTP.name();
            case "/api/saved/payee/save":
                return ActivityLogAction.SAVE_BENEFICIARY.name();
            case "/api/saved/payee/change/status":
                return ActivityLogAction.PAYEE_CHANGE_STATUS.name();
            case "/api/saved/credit/card/create":
                return ActivityLogAction.SAVE_CARD_BENEFICIARY.name();
            case "/api/saved/credit/card/change/status":
                return ActivityLogAction.CARD_BENEFICIARY_CHANGE_STATUS.name();

            case "/api/standing-order/all":
                return ActivityLogAction.STANDING_ORDERS_LIST.name();
            case "/api/standing-order/delete":
                return ActivityLogAction.STANDING_ORDERS_DELETE.name();

            case "/api/transaction/all":
                return ActivityLogAction.TRANSACTIONS_LIST.name();
            case "/api/transaction/change/paymentstatus":
                return ActivityLogAction.CHANGE_PAYMENT_STATUS.name();

            case "/3ds/callback":
                return ActivityLogAction.TRIPLE_DS_CALLBACK.name();

            case "/api/user/verify":
                return ActivityLogAction.VERIFY.name();
            case "/api/user/register/otp":
                return ActivityLogAction.SEND_OTP.name();
            case "/api/user/register/verify":
                return ActivityLogAction.VERIFY_OTP.name();
            case "/api/user/register/validate":
                return ActivityLogAction.VALIDATE_USER_DETAILS.name();
            case "/api/user/register":
                return ActivityLogAction.SIGN_UP.name();
            case "/api/user/device/nickname":
                return ActivityLogAction.DEVICE_NICKNAME.name();
            case "/api/user/login/mobile":
                return ActivityLogAction.LOGIN.name();
            case "/api/user/login":
                return ActivityLogAction.LOGIN.name();
            case "/api/user/upload/images":
                return ActivityLogAction.UPLOAD_RESOURCES.name();
            case "/api/user/change/nic/img":
                return ActivityLogAction.CHANGE_NIC_IMAGES.name();
            case "/api/user/nic/verify":
                return ActivityLogAction.NIC_VERIFY.name();
            case "/api/user/change/mobile/send/otp/old":
                return ActivityLogAction.SEND_OTP.name();
            case "/api/user/change/mobile/verify/old/otp":
                return ActivityLogAction.VERIFY_OTP.name();
            case "/api/user/change/mobile/send/otp/new":
                return ActivityLogAction.SEND_OTP.name();
            case "/api/user/change/mobile":
                return ActivityLogAction.CHANGE_MOBILE.name();
            case "/api/user/change/profile/image":
                return ActivityLogAction.CHANGE_PROFILE_IMAGE.name();
            case "/api/user/device/verify":
                return ActivityLogAction.DEVICE_VERIFY.name();
            case "/api/user/verify/username":
                return ActivityLogAction.VERIFY.name();
            case "/api/user/forgot/password":
                return ActivityLogAction.SEND_OTP.name();
            case "/api/user/forgot/password/verify":
                return ActivityLogAction.VERIFY_OTP.name();
            case "/api/user/mobile/change/password":
                return ActivityLogAction.CHANGE_PIN.name();
            case "/api/user/refreshtoken":
                return ActivityLogAction.GET_REFRESH_TOKEN.name();
            case "/api/user/login/switch":
                return ActivityLogAction.LOGIN_SWITCH.name();
            case "/api/user/login/init":
                return ActivityLogAction.LOGIN_INIT.name();
            case "/api/user/change/pin":
                return ActivityLogAction.CHANGE_PIN.name();
            case "/api/user/device/list":
                return ActivityLogAction.LOAD_DEVICE_LIST.name();
            case "/api/user/get/completed/step":
                return ActivityLogAction.GET_SIGNUP_GUIDE.name();

            default:
                return null;
        }
    }
}
