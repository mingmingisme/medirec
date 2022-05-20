from utils import *
import torch
import numpy as np
import cv2
import pydicom
import matplotlib.pyplot as plt
from skimage import img_as_float32 as img_as_float
import sys
import os


def norm(image):
    return (image - image.min()) / (image.max() - image.min())


def npy_sino_to_net(inputUrl, demoUrl, outputUrl, model_param):
    # sinogram
    sinogram = torch.Tensor(np.load(inputUrl)).unsqueeze(0).unsqueeze(0).to('cuda:0')
    plt.imsave(demoUrl, sinogram.to('cpu').squeeze(0).squeeze(0).numpy(), cmap='gray')

    # sparse view fbp
    sparse_view_fbp = norm(get_fbp(sinogram, n_angles=128))

    # load model parameters
    net = FBPCONVNet().to('cuda:0')
    net.load_state_dict(torch.load(model_param), strict=True)
    net.eval()
    with torch.no_grad():  # 验证的时候不用计算梯度，取消掉它减少时间和空间的损耗
        output = net(sparse_view_fbp)
    output = output.to('cpu').squeeze(0).squeeze(0).numpy()
    plt.imsave(outputUrl, output, cmap='gray')


if __name__ == '__main__':
    print(f'Current path is {os.getcwd()}')
    inputUrl = sys.argv[1]
    demoUrl = sys.argv[2]
    outputUrl = sys.argv[3]
    model_param = sys.argv[4]
    print('Converting image from {} to {}...'.format(inputUrl, outputUrl))
    npy_sino_to_net(inputUrl, demoUrl, outputUrl, model_param)
    print("Finish conversion")
