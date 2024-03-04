import React, { useState, useEffect } from "react";
import axios from "axios";
import CreateContest from "./CreateContest";

function ParentComponent() {
  const [contests, setContests] = useState([]);

  useEffect(() => {
    // Fetch contest data from API
    const fetchContests = async () => {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_BACKEND}/api/v1/contests`
        );
        setContests(response.data);
      } catch (error) {
        console.error("Error fetching contests:", error);
      }
    };

    fetchContests();
  }, []);

  return (
    <div>
      <h1>Parent Component</h1>
      <CreateContest contests={contests} />
    </div>
  );
}

export default ParentComponent;
