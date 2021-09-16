package com.mobdeve.s16.group22.medelivery;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {
    public final static String
            INFO_COLLECTION = "information",
            USER_COLLECTION = "users",
            CART_COLLECTION = "cart",
            MYCART_COLLECTION = "myCart",
            TRANSACTION_COLLECTION = "transaction",
            MYTRANSACTION_COLLECTION = "myTransactions",
            ITEM_COLLECTION = "items",
            ITEMLIST_COLLECTION = "itemList",

            FINALINFO_DOCUMENT = "finalInformation",

            BODY_FIELD = "bodyText",
            AUTHORA_FIELD = "authorA",
            EMAILA_FIELD = "emailA",
            AUTHORB_FIELD = "authorB",
            EMAILB_FIELD = "emailB",
            FNAME_FIELD = "fname",
            LNAME_FIELD = "lname",
            EMAIL_FIELD = "email",
            AGE_FIELD = "age",
            ADDRESS_FIELD = "address",
            CQUANTITY_FIELD = "cartQuantity",
            CPRICE_FIELD = "cartPrice",
            CID_FIELD = "cartUid",
            CNAME_FIELD = "cartName",
            IQUANTITY_FIELD = "itemQuantity",
            FILLER_FIELD = "filler",
            DATE_FIELD = "date",
            STATUS_FIELD = "status",
            TID_FIELD = "transactionID",
            SUBTOTAL_FIELD = "totalAmount",
            FEEDBACK_FIELD = "feedback",
            RATING_FIELD = "rating",

            DATA_INTENT = "data",
            TRANSACTION_INTENT = "TRANSACTION_REFERENCE";

    public static FirebaseFirestore getFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }

    public static FirebaseStorage getStorageInstance() {
        return FirebaseStorage.getInstance();
    }

    public static FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    public static FirebaseUser getFirebaseUser() {
        return getFirebaseAuth().getCurrentUser();
    }

    public static CollectionReference getCartCollectionReference() {
        return getFirestoreInstance().collection(CART_COLLECTION);
    }

    public static CollectionReference getInfoCollectionReference() {
        return getFirestoreInstance().collection(INFO_COLLECTION);
    }

    public static CollectionReference getItemsCollectionReference() {
        return getFirestoreInstance().collection(ITEM_COLLECTION);
    }

    public static CollectionReference getTransactionCollectionReference() {
        return getFirestoreInstance().collection(TRANSACTION_COLLECTION);
    }

    public static CollectionReference getUserCollectionReference() {
        return getFirestoreInstance().collection(USER_COLLECTION);
    }

    public static CollectionReference getMyCartCollectionReference() {
        return getCartCollectionReference().document(getUserID()).collection(MYCART_COLLECTION);
    }

    public static CollectionReference getItemListCollectionReference(String _id) {
        return getMyTransactionCollectionReference().document(_id).collection(ITEMLIST_COLLECTION);
    }

    public static CollectionReference getMyTransactionCollectionReference() {
        return getTransactionCollectionReference().document(getUserID()).collection(MYTRANSACTION_COLLECTION);
    }

    public static String getUserID() {
        return getFirebaseUser().getUid();
    }

    public static DocumentReference getInfoDocumentReference() {
        return getInfoCollectionReference().document(FINALINFO_DOCUMENT);
    }

    public static DocumentReference getUserDocumentReference() {
        return getUserCollectionReference().document(getUserID());
    }

    public static DocumentReference getCartDocumentReference() {
        return getCartCollectionReference().document(getUserID());
    }

    public static DocumentReference getTransactionDocumentReference() {
        return getTransactionCollectionReference().document(getUserID());
    }

    public static DocumentReference getTransactionItemDocumentReference(String _id, String cartId) {
        return getItemListCollectionReference(_id).document(cartId);
    }

    public static DocumentReference getItemDocumentReference(String _id) {
        return getItemsCollectionReference().document(_id);
    }

    public static DocumentReference getMyCartDocumentReference(String _id) {
        return getMyCartCollectionReference().document(_id);
    }

    public static DocumentReference getMyTransactionDocumentReference(String _id) {
        return getMyTransactionCollectionReference().document(_id);
    }
}
