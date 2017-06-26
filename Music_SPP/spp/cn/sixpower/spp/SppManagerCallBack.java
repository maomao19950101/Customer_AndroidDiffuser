package cn.sixpower.spp;



public abstract interface SppManagerCallBack
{
 /* public static final int BIND_SUCCESS = 0;
  public static final int BIND_TIME_OUT = 1;
  public static final int HAS_BIND = 2;
  public static final int CONNECT_TIME_OUT = 3;
  public static final int WRITE_SUCCESS = 0;
  public static final int WRITE_ERROR = 1;
  public static final int NOT_BIND = 2;
  public static final int BLUETOOTH_DISABLE = 3;
  public static final int NOT_CONNECT = 4;
  public static final int DEVICE_NO_REPONSE = 5;*/

  public abstract void occurrError(int paramInt, String paramString);

  public abstract void discoverDevice(ExpandDevice paramExpandDevice);

  public abstract void scanTimeOut();

  public abstract void connectedAndReady();

  public abstract void bindCallback(int paramInt);

  public abstract void connectLoss();
  public abstract void connectTimeOut();

}

