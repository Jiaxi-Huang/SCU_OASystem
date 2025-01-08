declare module '@/components/Weather/Location' {
    export const location: {
        initMap: (id: string) => any;
    };
    export const geocode: (lat: number, lng: number, callback: (err: Error | null, regeocode: any) => void) => void;
}
