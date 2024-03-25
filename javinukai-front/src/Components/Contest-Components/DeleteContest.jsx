import axios from "axios";

const DeleteContest = (contestId) => {
  axios
    .delete(`${import.meta.env.VITE_BACKEND}/contests/${contestId}`, {
      withCredentials: true,
    })
    .then((response) => {
      console.log("delete request status log", response);
    })
    .catch((error) => {
      console.log(error);
    });

  return (
    <>
      <div>q</div>
    </>
  );
};
export default DeleteContest;
