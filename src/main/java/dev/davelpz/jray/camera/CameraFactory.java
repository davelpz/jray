package dev.davelpz.jray.camera;

import com.google.inject.assistedinject.Assisted;

public interface CameraFactory {
    Camera create(@Assisted("hSize") int hSize,@Assisted("vSize")  int vSize,@Assisted("fieldOfView")  double fieldOfView);
}
