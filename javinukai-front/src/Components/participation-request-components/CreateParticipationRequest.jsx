import axios from "axios";

const CreateParticipationRequest = (requestId, setRequestStatus)=>{        
        axios
          .post(
            `${
              import.meta.env.VITE_BACKEND
            }/api/v1/request?contestId=${requestId}`,
            {},            
            { withCredentials: true }
          )
          .then((response) => {
            console.log("post response", response);
            setRequestStatus(response.data.requestStatus);
          })
          .catch((error) => {
            console.log("post error", error);
          });
}
export default CreateParticipationRequest