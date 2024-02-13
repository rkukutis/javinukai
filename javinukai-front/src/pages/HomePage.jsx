import React from 'react'
import cat1 from '../assets/cat1.jpg'
import cat2 from '../assets/cat2.jpg'
import cat3 from '../assets/cat3.jpg'
import Modal from "../Components/Modal"



function HomePage() {
  




return (
    <div className="flex flex-col items-center ">
      
      <div className="flex items-center mx-4 mb-4 justify-center  sm:flex-col  xl:flex-row">
        <img src={cat1} alt="cat1" className="max-w-full max-h-full object-cover w-16 md:w-48 lg:w-48" style={{ maxHeight: '80vh' }} />
        <div className="fle items-center justify-center p-5">
        <p className="ml-4">contest 2020</p>
        <p>Description</p>
        
        <Modal openModalText='Competition' confirmText="Confirm Participation" actionType="confirm"/>
            </div>
      </div>

     
      <div className="flex items-center mx-4 mb-4 sm:flex-col xl:flex-row ">
        <img src={cat2} alt="cat2" className="max-w-full max-h-full object-cover w-16 md:w-48 lg:w-48" style={{ maxHeight: '80vh' }} />
        <div className="fle items-center justify-center p-5"> 
        <p className="ml-4">contest 2091</p>
        <p>Description</p>
        <Modal openModalText='Competition' redirectText="Register Now"  actionType="redirect"/>
            
            </div>
      </div>

     
      <div className="flex items-center mx-4 sm:flex-col xl:flex-row">
        <img src={cat3} alt="cat3" className="max-w-full max-h-full object-cover w-16 md:w-48 lg:w-48" style={{ maxHeight: '80vh' }} />
        <div className="fle items-center justify-center p-5">
        <p className="ml-4">contest 2018</p>
        <p>Description</p>
        <Modal openModalText='Competition'/>
            
            </div>
      </div>
      
      
    </div>
    
)


}


export default HomePage