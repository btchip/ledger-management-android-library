package com.ledger.management.lib;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Query;
import retrofit2.http.Path;

import com.ledger.management.lib.model.GetDeviceVersionParameters;
import com.ledger.management.lib.model.GetCurrentFirmwareParameters;
import com.ledger.management.lib.model.GetApplicationsByDeviceParameters;
import com.ledger.management.lib.model.GetLatestFirmwareParameters;
import com.ledger.management.lib.model.DeviceVersion;
import com.ledger.management.lib.model.FinalFirmware;
import com.ledger.management.lib.model.LatestFirmware;
import com.ledger.management.lib.model.ApplicationVersionList;
import com.ledger.management.lib.model.McuVersion;
import com.ledger.management.lib.model.Category;

/**
 * \brief Communication interface with the enumeration service returning information about the available firmwares and applications for a given device version
 */
public interface ManagerService {

	/** 
	  * Get information about a device version
	  * @see DeviceVersion
	*/
	@POST("/api/get_device_version")
	Call<DeviceVersion> getDeviceVersion(@Body GetDeviceVersionParameters parameters, @Query("livecommonversion") long apiVersion);

	/**
	  * Get information about a firmware version for a device
	  * @see FinalFirmware
	  */
	@POST("/api/get_firmware_version")
	Call<FinalFirmware> getCurrentFirmware(@Body GetCurrentFirmwareParameters parameters, @Query("livecommonversion") long apiVersion);

	/**
	  * Get information about the available applications for a device
	  * @see ApplicationVersionList
	  */
	@POST("/api/get_apps")
	Call<ApplicationVersionList> getApplicationsByDevice(@Body GetApplicationsByDeviceParameters parameters, @Query("livecommonversion") long apiVersion);

	/**
	  * Get information about a firmware update bootstrap for a device
	  * @see LatestFirmware
	  */
	@POST("/api/get_latest_firmware")
	Call<LatestFirmware> getLatestFirmware(@Body GetLatestFirmwareParameters parameters, @Query("livecommonversion") long apiVersion);

	/**
	  * Get information about a firmware update for a device
	  * @see FinalFirmware
	  */
	@GET("/api/firmware_final_versions/{id}")
	Call<FinalFirmware> getFinalFirmwareById(@Path("id") long firmwareId, @Query("livecommonversion") long apiVersion);

	/**
	  * Get information about the available non secure firmware versions
	  * @see McuVersion
	  */
	@GET("/api/mcu_versions")
	Call<List<McuVersion>> getMcus(@Query("livecommonversion") long apiVersion);

	/**
	  * Get information about the available application categories
	  * @see Category
	  */
	@GET("/api/categories")
	Call<List<Category>> getCategories(@Query("livecommonversion") long apiVersion);
}
