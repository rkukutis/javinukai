import axios from "axios";

const deleteContest = (contestId, queryClient) => {

  axios
    .delete(`${import.meta.env.VITE_BACKEND}/api/v1/contests/${contestId}/delete`, {
      withCredentials: true,
    })
    .then(() => {
      queryClient.invalidateQueries(['contests']);      
    })
    .catch((error) => {
      console.log(error);
    });
  
};
export default deleteContest;
