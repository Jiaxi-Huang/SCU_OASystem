export const location = {
    initMap(id) {
        return new Promise((resolve, reject) => {
            const mapObj = new AMap.Map(id, {});
            AMap.plugin('AMap.Geolocation', function () {
                const geolocation = new AMap.Geolocation({
                    enableHighAccuracy: true,
                    timeout: 10000,
                    maximumAge: 0,
                    convert: true,
                    showButton: false,
                    buttonPosition: 'LB',
                    buttonOffset: new AMap.Pixel(10, 20),
                    showMarker: false,
                    showCircle: false,
                    panToLocation: true,
                    zoomToAccuracy: true
                });
                mapObj.addControl(geolocation);
                geolocation.getCurrentPosition((status, result) => {
                    if (status === 'complete' && result.position) {
                        resolve(geolocation);
                    } else {
                        console.error('Geolocation failed:', result);
                        reject(result);
                    }
                });
            });
        });
    }
};

export const geocode = (lat, lng, callback) => {
    AMap.plugin('AMap.Geocoder', function () {
        const geocoder = new AMap.Geocoder();
        geocoder.getAddress([lng, lat], (status, result) => {
            if (status === 'complete' && result.regeocode) {
                callback(null, result.regeocode);
            } else {
                callback(new Error('逆地理编码失败'));
            }
        });
    });
};
