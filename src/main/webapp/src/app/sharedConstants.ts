import villes from '../assets/js/villes.json';

export interface Annonce {
  announceType:	string,
  arrival:	string,
  categories:	Array<string>,
  departure:	string,
  description:	string,
  endDate: number,
  goldPrice:	number,
  preniumPrice:	number,
  price: number,
  startDate: number,
  transport: string,
  userId:	number,
  weight: number
};

export interface User{
  email: string,
  password: string,
  provider: string,
  socialId: string,
  username: string
}
export class UsersUtils {

  static createUser(email: string, firstName: string, gender: string, lastName: string, password: string, phone: string, username: string,
    provider: string="", role: string="USER", socialId: string="") {
    return {email, firstName,gender,lastName,password,phone,username,provider, role, socialId};
  }
  static logUser(password: string,username: string,email: string="test@ya.nn",provider: string="", socialId: string=""):User {
    return {password,username,email,provider,socialId};
  }
  static createAnnounce(announceType: string, arrival: string, categories: Array<string>, departure: string, description: string, endDate: number,
    goldPrice: number, preniumPrice: number, price: number, startDate: number,transport:string,userId:number,weight:number): Annonce {
    return {announceType, arrival,categories,departure,description,endDate,goldPrice,preniumPrice,price,startDate,transport,userId,weight};
  }
}

export class SharedConstants {

  public static Categories = [
    {
      value: "",
      description: "Indifférent"
    },
     {
       value: "AUTRES",
       description: "Autres"
     },
     {
       value: "ELECTRONIQUE",
       description: "Electronique"
     },
     {
       value: "VETEMENTS",
       description: "Vetements"
     },
     {
       value: "BIJOUX",
       description: "Bijoux"
     },
     {
       value: "DOCUMENTS",
       description: "Documents"
     }
  ];
  public static Transports = [
     {
       value: "",
       description: "Indifférent"
     },
     {
       value: "AUTO",
       description: "Routier"
     },
     {
       value: "PLANE",
       description: "Aerien"
     },
     {
       value: "NAVE",
       description: "Maritime"
     }
  ];
  public static Villes = villes;
//   [
//     {
//       id: 1,
//       name: 'MILAN'
//     },
//     {
//       id: 2,
//       name: 'BOLOGNE'
//     },
//     {
//       id: 3,
//       name: 'DOUALA'
//     },
//     {
//       id: 4,
//       name: 'YAOUNDE'
//     },
//     {
//       id: 5,
//       name: 'PARIS'
//     }
//  ];
}

export class SharedService {

  checkImage(user: any, product: boolean = false): boolean{

    return ((!product && user && user.image != null && user.image.origin != null && user.image.picByte != null) || (product && user && user.origin != null && user.picByte != null));
  }
  // getPreviousAndCurrentUrl(){
  //   let router: Router;
  //   router.events.pipe(filter((evt: any) => evt instanceof RoutesRecognized), pairwise()).subscribe((events: RoutesRecognized[]) => {
  //     // console.log('previous url', events[0].urlAfterRedirects);
  //     // console.log('current url', events[1].urlAfterRedirects);
  //     return [events[0].urlAfterRedirects,events[1].urlAfterRedirects];
  //   });
    
  // }
}