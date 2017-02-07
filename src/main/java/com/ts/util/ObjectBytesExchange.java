package com.ts.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ObjectBytesExchange {
	private static final Log log = LogFactory.getLog(ObjectBytesExchange.class);

	public static byte[] toByteArray(Object obj) {
		if (!(obj instanceof Serializable)) {
			throw new IllegalArgumentException(
					ObjectBytesExchange.class.getSimpleName()
							+ " requires a Serializable payload "
							+ "but received an object of type ["
							+ obj.getClass().getName() + "]");
		}

		byte[] bytes = null;
		ObjectOutputStream oos = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			oos = null;
			bos.close();
			bos = null;

			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toByteArray() >>> oos.close() throw a IOException.",e);
				}
			}
			if (null != bos)
				try {
					bos.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toByteArray() >>> bos.close() throw a IOException.",e);
				}
		} catch (IOException ex) {
			log.error("ObjectBytesExchange.toByteArray() throw a IOException.",
					ex);

			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toByteArray() >>> oos.close() throw a IOException.",e);
				}
			}
			if (null != bos)
				try {
					bos.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toByteArray() >>> bos.close() throw a IOException.",e);
				}
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toByteArray() >>> oos.close() throw a IOException.",e);
				}
			}
			if (null != bos) {
				try {
					bos.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toByteArray() >>> bos.close() throw a IOException.",e);
				}
			}
		}
		return bytes;
	}

	public static Object toObject(byte[] bytes) {
		Object obj = null;
		ObjectInputStream ois = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		try {
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			ois = null;
			bis.close();
			bis = null;

			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toObject() >>> ois.close() throw a IOException.",e);
				}
			}
			if (null != bis)
				try {
					bis.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toObject() >>> bis.close() throw a IOException.",e);
				}
		} catch (IOException ex) {
			log.error("ObjectBytesExchange.toObject() throw a IOException.", ex);

			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toObject() >>> ois.close() throw a IOException.",e);
				}
			}
			if (null != bis)
				try {
					bis.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toObject() >>> bis.close() throw a IOException.",e);
				}
		} catch (ClassNotFoundException ex) {
			log.error("ObjectBytesExchange.toObject() throw a ClassNotFoundException.",	ex);

			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toObject() >>> ois.close() throw a IOException.",e);
				}
			}
			if (null != bis)
				try {
					bis.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toObject() >>> bis.close() throw a IOException.",e);
				}
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toObject() >>> ois.close() throw a IOException.",e);
				}
			}
			if (null != bis) {
				try {
					bis.close();
				} catch (IOException e) {
					log.error("ObjectBytesExchange.toObject() >>> bis.close() throw a IOException.",e);
				}
			}
		}
		return obj;
	}
}