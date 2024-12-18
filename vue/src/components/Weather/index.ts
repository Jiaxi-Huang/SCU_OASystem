import request from '@/utils/request';

const weatherApi = {
    getWeather: '/api/weather',
    localHost: 'http://localhost:8080',
    modifyWeather: '/api/weather/modify',
    addWeather: '/api/weather/add',
    deleteWeather: '/api/weather/delete'
};

class WeatherService {
    static getWeather(city: string) {
        return request({
            url: `${weatherApi.localHost}${weatherApi.getWeather}?city=${city}`,
            method: 'GET',
            json: true,
        }).then((res) => {
            if (res.status === 200) {
                return res.data;
            }
            return null;
        });
    }

    static postModifyWeather(record: any) {
        return request({
            url: `${weatherApi.localHost}${weatherApi.modifyWeather}`,
            method: 'POST',
            json: true,
            data: record,
        }).then((res) => {
            if (res.status === 200) {
                return res.data;
            }
            return null;
        });
    }

    static addWeather(record: any) {
        return request({
            url: `${weatherApi.localHost}${weatherApi.addWeather}`,
            method: 'POST',
            json: true,
            data: record,
        }).then((res) => {
            if (res.status === 200) {
                return res.data;
            }
            return null;
        });
    }

    static deleteWeather(record: any) {
        return request({
            url: `${weatherApi.localHost}${weatherApi.deleteWeather}`,
            method: 'POST',
            json: true,
            data: record,
        }).then((res) => {
            if (res.status === 200) {
                return res.data;
            }
            return null;
        });
    }
}

export default WeatherService;
