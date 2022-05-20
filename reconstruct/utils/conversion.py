import numpy as np
import torch
from torch_radon import RadonFanbeam

#---------------------------------------------------------------------------------------------------------------------------
# Fan Beam Projection
def get_fbp(x,n_angles=512,image_size=512):
    fanbeam_angles = np.linspace(0, 2*np.pi, n_angles, endpoint=False)
    radon_fanbeam = RadonFanbeam(image_size, fanbeam_angles, source_distance=512, det_distance=512, det_spacing=2.5)

    filtered_sinogram = radon_fanbeam.filter_sinogram(x)
    fbp = radon_fanbeam.backprojection(filtered_sinogram)
    return fbp

def get_sinogram(x,n_angles=512,image_size=512):
    device = torch.device('cuda')
    x = torch.FloatTensor(x).to(device)
    
    fanbeam_angles = np.linspace(0, 2*np.pi, n_angles, endpoint=False)
    radon_fanbeam = RadonFanbeam(image_size, fanbeam_angles, source_distance=512, det_distance=512, det_spacing=2.5)
    sinogram = radon_fanbeam.forward(x)

    return sinogram.cpu().numpy()