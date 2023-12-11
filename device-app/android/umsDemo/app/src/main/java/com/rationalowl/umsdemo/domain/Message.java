package com.rationalowl.umsdemo.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message implements Cloneable, Parcelable {
    private static final String ID_KEY = "mId";
    private static final String TYPE_KEY = "type";
    private static final String TITLE_KEY = "title";
    private static final String BODY_KEY = "body";
    private static final String SENT_AT_KEY = "st";
    private static final String IMG_ID_KEY = "ii";

    private final String id;
    private final MessageType type;
    private final String title, body;
    private Date sentAt, receivedAt;
    @Nullable
    private final String imageId;

    private boolean isRead;
    @Nullable
    private Date alimTalkSentAt, munjaSentAt;
    @Nullable
    private Integer munjaType;

    @JsonCreator
    public Message(@JsonProperty("id") String id,
                   @JsonProperty("type") MessageType type,
                   @JsonProperty("title") String title,
                   @JsonProperty("body") String body,
                   @JsonProperty("sentAt") Date sentAt,
                   @JsonProperty("receivedAt") Date receivedAt,
                   @JsonProperty("imageId") @Nullable String imageId) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.body = body;
        this.sentAt = sentAt;
        this.receivedAt = receivedAt;
        this.imageId = imageId;
    }

    public Message(Map<String, String> map) {
        this.id = map.get(ID_KEY);
        this.type = map.get(TYPE_KEY).equals("2") ? MessageType.EMERGENCY : MessageType.NORMAL;
        this.title = map.get(TITLE_KEY);
        this.body = map.get(BODY_KEY);

        final String sendAtStr = map.get(SENT_AT_KEY);
        this.sentAt = new Date(Long.parseLong(sendAtStr));
        this.receivedAt = new Date();
        this.imageId = map.get(IMG_ID_KEY);
    }

    protected Message(Parcel in) {
        id = in.readString();
        type = MessageType.values()[in.readInt()];
        title = in.readString();
        body = in.readString();
        sentAt = new Date(in.readLong());
        receivedAt = new Date(in.readLong());
        imageId = in.readString();

        isRead = in.readByte() != 0;

        final long alimTalkSentAtMill = in.readLong();
        if (alimTalkSentAtMill != -1) {
            alimTalkSentAt = new Date(alimTalkSentAtMill);
        }

        final long munjaSentAtMill = in.readLong();
        if (munjaSentAtMill != -1) {
            munjaSentAt = new Date(munjaSentAtMill);
        }

        final int munjaTypeInt = in.readInt();
        if (munjaTypeInt != -1) {
            munjaType = munjaTypeInt;
        }
    }

    @NonNull
    @Override
    public Message clone() throws CloneNotSupportedException {
        final Message clone = (Message) super.clone();

        if (this.sentAt != null) {
            clone.sentAt = (Date) this.sentAt.clone();
        }

        if (this.receivedAt != null) {
            clone.receivedAt = (Date) this.receivedAt.clone();
        }

        if (this.alimTalkSentAt != null) {
            clone.alimTalkSentAt = (Date) this.alimTalkSentAt.clone();
        }

        if (this.munjaSentAt != null) {
            clone.munjaSentAt = (Date) this.munjaSentAt.clone();
        }

        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) return false;

        final Message other = (Message) obj;
        return Objects.equals(id, other.id) &&
                type == other.type &&
                Objects.equals(title, other.title) &&
                Objects.equals(body, other.body) &&
                Objects.equals(sentAt, other.sentAt) &&
                Objects.equals(receivedAt, other.receivedAt) &&
                Objects.equals(imageId, other.imageId) &&
                isRead == other.isRead &&
                Objects.equals(alimTalkSentAt, other.alimTalkSentAt) &&
                Objects.equals(munjaSentAt, other.munjaSentAt) &&
                Objects.equals(munjaType, other.munjaType);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public MessageType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    @Nullable
    public String getImageId() {
        return imageId;
    }

    @JsonProperty("isRead")
    public boolean isRead() {
        return isRead;
    }

    @Nullable
    public Date getAlimTalkSentAt() {
        return alimTalkSentAt;
    }

    @Nullable
    public Date getMunjaSentAt() {
        return munjaSentAt;
    }

    @Nullable
    public Integer getMunjaType() {
        return munjaType;
    }

    public void setAsRead() {
        isRead = true;
    }

    public void setAlimTalkSentAt(@NonNull Date value) {
        alimTalkSentAt = value;
    }

    public void setMunjaSentAt(@NonNull Date value) {
        munjaSentAt = value;
    }

    public void setMunjaType(@NonNull Integer value) {
        munjaType = value;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(type.ordinal());
        dest.writeString(title);
        dest.writeString(body);
        dest.writeLong(sentAt.getTime());
        dest.writeLong(receivedAt.getTime());
        dest.writeString(imageId);

        dest.writeByte((byte) (isRead ? 1 : 0));
        dest.writeLong(alimTalkSentAt != null ? alimTalkSentAt.getTime() : -1);
        dest.writeLong(munjaSentAt != null ? munjaSentAt.getTime() : -1);
        dest.writeInt(munjaType != null ? munjaType : -1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
