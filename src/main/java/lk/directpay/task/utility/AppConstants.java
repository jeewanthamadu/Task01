package lk.directpay.task.utility;

public class AppConstants {

    //SMS Service Utils
    public static final String SMS_MASK = "NSB";
    public static final String SMS_LANGUAGE_ENGLISH = "1";

    //Page Constants
    public static final int BUTTONS_TO_SHOW = 5;
    public static final int INITIAL_PAGE = 1;
    public static final int INITIAL_PAGE_SIZE = 10;
    public static final int[] PAGE_SIZES = {5, 10, 20};

    //Firebase Notification
    public static final String FCM_PRIORITY = "high";

    //Messages
    public static final String DEVICE_CHANGE_MSG = "Dear NSBPAY App User , Your registered mobile device has been changed. If you did not initiate this please contact customer Support- 0112 379 379 or 1972";
    public static final String LOGIN_ATTEMPTS_EXCEEDED = "Dear NSBPAY App User, Your account has been locked out due to invalid login attempts. Please contact customer Support - 0112 379 379 or 1972";

    public static final String NSB_BANK_CODE = "7719";
    public static final String NSB_BANK_NAME = "National Savings Bank";
    public static final String NSB_CORE_BANK_FROM_ACCOUNT_NAME = "NSB CORE BANK SOURCE ACCOUNT";
    public static final String NSB_CORE_BANK_TO_ACCOUNT_NAME = "NSB CORE BANK DESTINATION ACCOUNT";
    public static final String NSB_CEFT_CODE = "6719";
    public static final String BOX_ACCOUNT_FOR_HIGHWAY = "76991963";
    public static final String NSB_FUND_TRANSFER_COLLECTION_ACCOUNT_NAME = "FUND TRANSFER COLLECTION ACCOUNT";
    public static final String BILL_PAYMENT_COLLECTION_ACCOUNT_NAME = "BILL PAYMENT COLLECTION ACCOUNT";
    public static final String PARTIAL_REFUND_ACCOUNT_NAME = "PARTIAL REFUND ACCOUNT";
    public static final String CREDIT_CARD_PAYEMNT_COLLECTION_ACCOUNT_NAME = "CREDIT CARD PAYEMNT COLLECTION ACCOUNT";

    //consents
    public static final String ACCOUNT_LINK_CONSENT = "You have some other bank accounts linked to NSBPay. Press \"I agree\" to keep these accounts linked.";

    //config param names
    public static final String FUND_TRANSFER_RECEIVING_MID = "FUND_TRANSFER_RECEIVING_MID";
    public static final String FUND_TRANSFER_SENDING_ACCOUNT = "FUND_TRANSFER_SENDING_ACCOUNT";
    public static final String NSB_TO_OTHER_COMMISSION = "NSB_TO_OTHER_COMMISSION";
    public static final String OTHER_TO_OTHER_COMMISSION = "OTHER_TO_OTHER_COMMISSION";
    public static final String NSB_TO_NSB_COMMISSION = "NSB_TO_NSB_COMMISSION";
    public static final String OTHER_TO_NSB_COMMISSION = "OTHER_TO_NSB_COMMISSION";
    public static final String COLLECTION_ACCOUNT_NSB = "COLLECTION_ACCOUNT_NSB";
    public static final String BILL_COMMISSION_SETTLE_ACCOUNT_NSB = "BILL_COMMISSION_SETTLE_ACCOUNT_NSB";
    public static final String FT_COMMISSION_SETTLE_ACCOUNT_NSB = "FT_COMMISSION_SETTLE_ACCOUNT_NSB";
    public static final String BILL_PAYMENT_COLLECTION_ACCOUNT_NSB = "BILL_PAYMENT_COLLECTION_ACCOUNT_NSB";
    public static final String PARTIAL_REFUND_ACCOUNT_NSB = "PARTIAL_REFUND_ACCOUNT_NSB";
    public static final String HIGHWAY_COLLECTION_ACCOUNT = "HIGHWAY_COLLECTION_ACCOUNT";
    public static final String HIGHWAY_COLLECTION_BANK = "HIGHWAY_COLLECTION_BANK";
    public static final String HIGHWAY_TOPUP_COLLECTION_ACCOUNT = "HIGHWAY_TOPUP_COLLECTION_ACCOUNT";
    public static final String HIGHWAY_TOPUP_COLLECTION_ACCOUNT_NAME = "HIGHWAY_TOPUP_COLLECTION_ACCOUNT_NAME";
    public static final String HIGHWAY_TOPUP_COLLECTION_BANK = "HIGHWAY_TOPUP_COLLECTION_BANK";
    public static final String CREDIT_CARD_PAYMENT_COLLECTION_ACCOUNT_NSB = "CREDIT_CARD_PAYMENT_COLLECTION_ACCOUNT_NSB";
    public static final String COLLECTION_ACCOUNT_NSB_BRANCH_CODE = "COLLECTION_ACCOUNT_NSB_BRANCH_CODE";
    public static final String COLLECTION_ACCOUNT_OTHER = "COLLECTION_ACCOUNT_OTHER";
    public static final String COLLECTION_ACCOUNT_OTHER_BRANCH_CODE = "COLLECTION_ACCOUNT_OTHER_BRANCH_CODE";
    public static final String CEFT_FEE = "CEFT_FEE";
    public static final String COLLECTION_ACCOUNT_MERCHANT_ID = "COLLECTION_ACCOUNT_MERCHANT_ID";
    public static final String CREDIT_CARD_COLLECTION_ACCOUNT_MERCHANT_ID = "CREDIT_COLLECTION_ACCOUNT_MERCHANT_ID";
    public static final String LECO_ACCESS_TOKEN = "LECO_ACCESS_TOKEN";
    public static final String LECO_ACCESS_TOKEN_EXPIRE_TIME = "LECO_ACCESS_TOKEN_EXPIRE_TIME";

    public static final String CREDIT_CARD_PAYMENT_FROM_NSB_COMMISSION = "CREDIT_CARD_PAYMENT_FROM_NSB_COMMISSION";
    public static final String CREDIT_CARD_PAYMENT_FROM_OTHER_COMMISSION = "CREDIT_CARD_PAYMENT_FROM_OTHER_COMMISSION";

    public static final String COLLECTION_ACCOUNT_SAVINGS_TILL = "COLLECTION_ACCOUNT_SAVINGS_TILL";

    //fund transfer description
    public static final String USER_TO_INTERMEDIATE_ACCOUNT = "User to Intermediate account";
    public static final String INTERMEDIATE_ACCOUNT_TO_BENEFICIARY = "Intermediate account to beneficiary";
    public static final String USER_TO_BENEFICIARY = "User to beneficiary";
    public static final String INTERMEDIATE_ACCOUNT_TO_USER = "Intermediate account to User";

    //kafka
    public static final String KAFKA_TOP_QR_TRANSACTION = "nsb-qr-transaction";

    public static final int HTTP_TIMEOUT = 60 * 1000;
    public static final String NSB_FUND_TRANSFER_COLLECTION_ACCOUNT_MID = "NSB_FUND_TRANSFER_COLLECTION_ACCOUNT_MID";

    public static String getResponseMessageFromCode(String responseCode){
        switch (responseCode){
            case "00":
                return "Success";
            case "01":
                return "Transaction Failed";
            case "02":
                return "Transaction Declined";
            case "03":
                return "Invalid Request Data";
            case "04":
                return "Duplicate Transaction";
            case "05":
                return "Do not Honor";
            case "11":
                return "Invalid Mobile Number";
            case "12":
                return "Invalid Transaction";
            case "13":
                return "Invalid Amount";
            case "14":
                return "Invalid Account Number";
            case "15":
                return "No pending transaction for user";
            case "17":
                return "Invalid Message Type";
            case "19":
                return "Invalid Transaction Type";
            case "21":
                return "Invalid Merchant Code";
            case "22":
                return "Invalid NIC";
            case "23":
                return "User does not exist";
            case "24":
                return "NIC already registered";
            case "25":
                return "Account Already Registered";
            case "26":
                return "Bank Does not exist";
            case "27":
                return "Username already registered";
            case "39":
                return "No Account";
            case "40":
                return "Account Already Verified";
            case "41":
                return "Token expired";
            case "42":
                return "Token not valid";
            case "43":
                return "Bad Credentials";
            case "44":
                return "Account Already Removed";
            case "51":
                return "Insufficient Account Balance";
            case "60":
                return "JP Daily Transaction Limit Exceeds";
            case "61":
                return "JP Per Transaction Limit Exceeds";
            case "62":
                return "Transaction Declined by Beneficiary Bank";
            case "70":
                return "User signature for device not exist";
            case "71":
                return "User account signature for device not exist";
            case "72":
                return "Transaction signature does not exist";
            case "89":
                return "Mobile number does not exist";
            case "91":
                return "Time Out";
            case "96":
                return "System Malfunction";
            case "97":
                return "System Not Available";
            case "99":
                return "Not Authorized";
            default:
                return "No response received";

        }
    }

    public static String getStatementDescription(String code){
        switch (code){
            case "MIN":
                return "FD - Monthly Interest";
            case "RNW":
                return "FD - Renewal";
            case "DEP":
                return "Deposit";
            case "WTH":
                return "Withdrawal";
            case "PAY":
                return "Payments";
            case "SUP":
                return "Supplementary Txn";
            case "ADJ":
                return "Adjustment - Transaction";
            case "CLP":
                return "FD Closing - Penal";
            case "SOD":
                return "Standing Order";
            case "CLS":
                return "Savings Closing";
            case "ACT":
                return "Activate - FD";
            case "FDS":
                return "FD - Suspense";
            case "WDW":
                return "Withdrawal";
            case "IBT":
                return "IBT (Manual - (BO))";
            case "ADV":
                return "Advance - Staff";
            case "LON":
                return "Loan (LAD)";
            case "REC":
                return "Recovery";
            case "BDP":
                return "Bills Deposited";
            case "BDW":
                return "Bills Withdrawals";
            case "BSC":
                return "Bills Sent for Collection";
            case "BP":
                return "Bills Purchased";
            case "BRL":
                return "Bills Realized";
            case "EXP":
                return "Expense";
            case "INC":
                return "Income";
            case "SIN":
                return "Savings Interest";
            case "MSC":
                return "Miscellaneous";
            case "CQD":
                return "Cheque Deposit";
            case "CQW":
                return "Withdrawal by Cheque";
            case "PEN":
                return "Pension";
            case "OBT":
                return "Savings Debit Tax";
            case "BNS":
                return "Bonus Interest";
            case "RIN":
                return "Reward Interest";
            case "RCD":
                return "IBT (Teller) - Cash Deposit";
            case "RCW":
                return "IBT (Teller) - Cash Withdrawal";
            case "RRV":
                return "IBT (Teller) Txn Reversal";
            case "SAL":
                return "Salary";
            case "NAV":
                return "Navy Salary";
            case "NSP":
                return "NSB Salary";
            case "GAJ":
                return "Gajasakthi Salary";
            case "BLC":
                return "Bill Payment - Utility";
            case "STK":
                return "Batch Txn for supplies items";
            case "MCD":
                return "Mobile cash deposit";
            case "MCW":
                return "Mobile cash withdrawal";
            case "MTD":
                return "Mobile Transfer - Cr";
            case "MTW":
                return "Mobile Transfer - Dr";
            case "MCR":
                return "Mobile Cash Reversal";
            case "MTR":
                return "Mobile Transfer Reversal";
            case "MRD":
                return "Mobile Remote Cash Deposit";
            case "MRW":
                return "Mobile Remote Cash Withdrawal";
            case "MRR":
                return "Mobile Remote Cash Reversal";
            case "FCW":
                return "Foreign Currency - Dr";
            case "FCD":
                return "Foreign Currency - Cr";
            case "FCR":
                return "Foreign Currency - Reversal";
            case "STD":
                return "SMS Transfer - Cr";
            case "STW":
                return "SMS Transfer - Dr";
            case "STR":
                return "SMS Transfer - Reversal";
            case "TTD":
                return "Telebanking Transfer - Cr";
            case "TTW":
                return "Telebanking Transfer - Dr";
            case "TTR":
                return "Telebanking txn - Reversal";
            case "TBD":
                return "Util. Bill Payment - Cr";
            case "TBW":
                return "Util. Bill Payment - Dr";
            case "TBR":
                return "Util.- Bill payment - Reversal";
            case "HLT":
                return "Housing Loan Transaction";
            case "PFR":
                return "Pension+ Fund Received";
            case "PPY":
                return "Pension+ Payment";
            case "ACW":
                return "ATM Cash Withdrawal";
            case "AIB":
                return "ATM balance Inquery";
            case "AIN":
                return "Savings Interest - Monthly";
            case "ARV":
                return "ATM Transaction Reversal";
            case "ATD":
                return "ATM Tranfer - Cr";
            case "ATM":
                return "ATM Transaction -  Adjustment";
            case "ATR":
                return "ATM Transfer - Reversal";
            case "ATW":
                return "ATM Transfer - Dr";
            case "BA":
                return "Branch Advice (BO)";
            case "COM":
                return "Commission Recovered";
            case "DTC":
                return "Transfer from Dormant";
            case "FC":
                return "Foriegn Currency Txn";
            case "HRN":
                return "Hapan - Ithurumithuru Renewal";
            case "ICD":
                return "Instant Cash Deposit";
            case "ICR":
                return "Instant Cash Reversal";
            case "ICW":
                return "Instant Cash Withdrawal";
            case "ISS":
                return "SLIP Manually Processed";
            case "ITD":
                return "Instant Fund Tranfer - Cr";
            case "LST":
                return "Internal Batch List";
            case "NTD":
                return "Internet Transfer - Cr";
            case "NTR":
                return "Internet Transfer Reversals";
            case "NTW":
                return "Internet Transfer - Dr";
            case "RVS":
                return "Teller Txn Reversal";
            case "SIS":
                return "Batch Process of sisudiriya";
            case "SLO":
                return "SLIP Outward";
            case "SLP":
                return "SLIP Transaction";
            case "TXR":
                return "Tax Refund";
            case "DTP":
                return "Debit Tax";
            case "ITW":
                return "Instant Fund Tranfer - Dr";
            case "EBL":
                return "Loan Transfer via Ebank";
            case "MED":
                return "Medical Payment";
            case "GRA":
                return "Gratuity Pension Transaction";
            case "GPN":
                return "Government Pensions";
            case "IRD":
                return "INWARD REMITTANCE DEPOSITS";
            case "IRW":
                return "INWARD REMITTANCE WITHDRAWALS";
            case "IRR":
                return "INWARD REMITTANCE REVERSALS";
            case "DIP":
                return "Divi surekum Interest Payable";
            case "DBF":
                return "Divi surekum Benefit";
            case "ITR":
                return "Instant Fund Transfer - Rvs";
            case "APD":
                return "Mobile APP Deposit";
            case "APW":
                return "Mobile APP Withdrawal";
            case "APR":
                return "Mobile APP Reversal";
            case "FTX":
                return "Foreign Transaction via Slip";
            case "DTR":
                return "Dormant Transfer to UDRA Ac";
            case "CFD":
                return "CEFT Deposit";
            case "CFR":
                return "CEFT Reversal";
            case "ACD":
                return "ATM Cash Deposit";
            case "AGR":
                return "Farmers Allowance";
            case "CFW":
                return "CEFT Withdrawal";
            case "ASW":
                return "Treasury Withdrawal";
            case "ASR":
                return "Treasury Reversal";
            case "ASD":
                return "Treasury Deposit";
            case "NID":
                return "New Internet Deposit";
            case "NIW":
                return "New Internet Withdrawal";
            case "NIR":
                return "New Internet Reversal";
            default:
                return "";
        }
    }
}
