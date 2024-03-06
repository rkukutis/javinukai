
import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import axios from "axios";
import FormFieldError from "../FormFieldError";
import EditCategoryModal from "./EditCategoryModal";
import CreateCategory from "./CreateCategory";


function CreateContest({ contestDTO }) {
  const { register, handleSubmit, formState: { errors } } = useForm();
  const [totalSubmissions, setTotalSubmissions] = useState(50);
  const [categoriesList, setCategoriesList] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [showCreateCategory, setShowCreateCategory] = useState(false);
  const [contestCreated, setContestCreated] = useState(false);
  

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      const response = await axios.get(`${import.meta.env.VITE_BACKEND}/api/v1/categories`, { withCredentials: true });
      setCategoriesList(response.data.content);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  const handleAddCategory = (category) => {
    const updatedCategoriesList = categoriesList.map(c => {
      if (c.id === category.id) {
        return {
          ...c,
          added: true
        };
      }
      return c;
    });
    setCategoriesList(updatedCategoriesList);
  };

  const handleRemoveCategory = (categoryId) => {
    const updatedCategoriesList = categoriesList.map(c => {
      if (c.id === categoryId) {
        return {
          ...c,
          added: false
        };
      }
      return c;
    });
    setCategoriesList(updatedCategoriesList);
  };

  const handleEditCategory = (category) => {
    setSelectedCategory(category);
    setShowModal(true);
  };

  const onSubmit = async (data) => {
    if (!showCreateCategory) {
      try {
        const startDate = new Date(data.startDate).toISOString();
        const endDate = new Date(data.endDate).toISOString();
  
        if (isNaN(Date.parse(startDate)) || isNaN(Date.parse(endDate))) {
          console.error('Invalid date values');
          return;
        }
  
        const contestData = {
          ...data,
          totalSubmissions,
          startDate,
          endDate,
          categories: categoriesList.filter(category => category.added)
        };
  
        const response = await axios.post(`${import.meta.env.VITE_BACKEND}/api/v1/contests`, contestData, { withCredentials: true });
        console.log('Contest created successfully:', response.data);
        setContestCreated(true); 
      } catch (error) {
        console.error('Error creating contest:', error);
      }
    }
  };

  const handleTotalSubmissionsChange = (e) => {
    const newValue = e.target.value;
    if (newValue >= 1) {
      setTotalSubmissions(newValue);
    }
  };
 
  const handleCreateCategory = () => {
    setShowCreateCategory(true);
  };
  const handleCloseCategory = () => {
    setShowCreateCategory(false);
  };


  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-md shadow-md">
      <h2 className="text-2xl font-semibold mb-4">Create Contest</h2>
      <form onSubmit={handleSubmit(onSubmit)}>
        
        <div className="mb-4">
          <label htmlFor="contestName" className="block text-sm font-medium text-gray-700">Contest Name:</label>
          <input
            type="text"
            id="contestName"
            {...register("contestName", { required: "Required"})}
            defaultValue={contestDTO && contestDTO.contestName}
            className="mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
          />
          {errors.contestName && (
            <FormFieldError>{errors.contestName.message}</FormFieldError>
          )}
        </div>
        <div className="mb-4">
          <label htmlFor="description" className="block text-sm font-medium text-gray-700">Description:</label>
          <textarea
            id="description"
            {...register("description", { required: "Required"}) }
            defaultValue={contestDTO && contestDTO.description}
            className="mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
          />
          {errors.description && (
            <FormFieldError>{errors.description.message}</FormFieldError>
          )}

        </div>
        <div className="grid grid-cols-2 gap-x-4 mb-4">
          <div>
            <label htmlFor="startDate" className="block text-sm font-medium text-gray-700">Start Date:</label>
            <input
              type="date"
              id="startDate" 
              {...register("startDate", { required: "Required", min: { value: 1 } })}
              className="mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
            />
            {errors.startDate && (
              <FormFieldError>{errors.startDate.message}</FormFieldError>
            )}
          </div>
          <div>
            <label htmlFor="endDate" className="block text-sm font-medium text-gray-700">End Date:</label>
            <input
              type="date"
              id="endDate" 
              {...register("endDate", { required: "Required", min: 1 })}
              className="mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
            />
            {errors.endDate && (
              <FormFieldError>{errors.endDate.message}</FormFieldError>
            )}
          </div>
        </div>
        <div className="mb-4">
          <label htmlFor="totalSubmissions" className="block text-sm font-medium text-gray-700">Total Submissions:</label>
          <input
            type="number"
            id="totalSubmissions"
            min="1"
            {...register("totalSubmissions", { required: "Required", min: 1 })}
            value={totalSubmissions}
            onChange={handleTotalSubmissionsChange}
            className="mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
          />
          {errors.totalSubmissions && (
              <FormFieldError>{errors.totalSubmissions.message}</FormFieldError>
            )}
        </div>
        <div>
      {showCreateCategory && (
        <div>
          <CreateCategory />
          <button onClick={handleCloseCategory}>Close</button>
        </div>
      )}
      
      <button onClick={handleCreateCategory}>Create New Category</button>
    </div>
       

        <div className="mb-4">
          <label htmlFor="searchCategory" className="block text-sm font-medium text-gray-700">
            Search Categories:
          </label>
          <input
            type="text"
            id="searchCategory"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
            placeholder="Search categories by name..."
          />
        </div>
        <div>
          {categoriesList
            .filter(category => category.categoryName.toLowerCase().includes(searchQuery.toLowerCase()))
            .map(category => (
              <div key={category.id} className="flex items-center justify-between border border-gray-200 p-2 rounded-md mb-2">
                <div>
                  <h3>{category.categoryName} - {category.type}</h3>
                  <p>Description: {category.description}</p>
                  <p>Total Submissions: {category.totalSubmissions}</p>
                </div>
                <div>
                  <button type="button" onClick={() => handleEditCategory(category)} className="bg-green-500 text-white px-4 py-2 rounded-md mr-2">Edit Category</button>
                  {category.added ? (
                    <button type="button" onClick={() => handleRemoveCategory(category.id)} className="bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600">Remove</button>
                  ) : (
                    <button type="button" onClick={() => handleAddCategory(category)} className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">Add</button>
                  )}
                </div>
              </div>
            ))}
        </div>
        {contestCreated && (
          <div className="bg-green-200 text-green-800 px-4 py-2 rounded-md mb-4">
            Contest created!
          </div>
        )}
       
          <div>
            <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">Create Contest</button>
          </div>
        
      </form>

      {showModal && (
        <div className="fixed top-0 left-0 w-full h-full flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-4 rounded shadow-lg">
            <EditCategoryModal
              category={selectedCategory}
              onClose={() => setShowModal(false)}
            /> 
          </div>
        </div>
      )}
    </div>
  );
}

export default CreateContest;
