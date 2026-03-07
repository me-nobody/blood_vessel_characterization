#!/bin/bash -l

############# SLURM SETTINGS #############
#SBATCH --account=none   
#SBATCH --job-name=check_gpu_resources   
#SBATCH --output=%x-%j_out.txt      
#SBATCH --error=%x-%j_err.txt       
#SBATCH --gres=gpu:a40:1 
#SBATCH --partition=gpu            
#SBATCH --time=0-06:00:00          
#SBATCH --mem-per-gpu=1G           
#SBATCH --nodes=1              
#SBATCH --ntasks=4             
#SBATCH --cpus-per-task=4     
#SBATCH --ntasks-per-node=4    

############# LOADING MODULES (optional) #############
# module purge
source /opt/gridware/depots/996bcebb/el7/pkg/apps/anaconda3/2023.03/bin/etc/profile.d/conda.sh
conda init bash
conda activate /mnt/scratch/users/ad394h/sharedscratch/anu
echo "Hello from $SLURM_JOB_NODELIST"
echo "conda environment is $CONDA_DEFAULT_ENV"
############################################################################
python3 /users/ad394h/Documents/scripts/check_GPU_resources.py

echo "this means code ran"
# https://stackoverflow.com/questions/66611439/how-to-check-if-nvidia-gpu-is-available-using-bash-script
echo $(nvdia-smi)
# Some distro requires that the absolute path is given when invoking lspci
# e.g. /sbin/lspci if the user is not root.
gpu=$(lspci | grep -i '.* vga .* nvidia .*')

shopt -s nocasematch

if [[ $gpu == *' nvidia '* ]]; then
  printf 'Nvidia GPU is present:  %s\n' "$gpu"
  
else
  printf 'Nvidia GPU is not present: %s\n' "$gpu"
  
fi

############# END #############
conda deactivate

echo "conda default environment is $CONDA_DEFAULT_ENV" 

echo "$CONDA_PREFIX" 
